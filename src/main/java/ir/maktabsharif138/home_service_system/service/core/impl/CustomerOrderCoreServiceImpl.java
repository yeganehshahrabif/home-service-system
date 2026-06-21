package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.entity.enums.OrderPaymentStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.repository.OfferRepository;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerOrderCoreServiceImpl implements CustomerOrderCoreService {

    private final ExpertRepository expertRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final ExpertCoreService expertCoreService;

    @Override
    @Transactional
    public CustomerOrder createOrder(CustomerOrder order) {

        if (order.getProposedPrice().compareTo(order.getHomeService().getBasePrice()) < 0) {
            throw new BadRequestException("Proposed price cannot be less than base price");
        }

        if (order.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start date must be in future");
        }

        if (Objects.isNull(order.getHomeService().getParentService())) {
            throw new BadRequestException("Order must be created for a sub service");
        }
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFERS);

        return customerOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerOrder findById(Long id) {

        return customerOrderRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Order not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findByCustomerId(Long customerId, Pageable pageable) {
        return customerOrderRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    @Transactional
    public CustomerOrder startOrder(Long orderId) {
        CustomerOrder order = findById(orderId);
        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_EXPERT) {
            throw new BadRequestException("Order is not waiting for expert");
        }

        if (Objects.isNull(order.getAcceptedOffer())) {
            throw new BadRequestException("Accepted offer not found");
        }

        if (LocalDateTime.now().isBefore(order.getAcceptedOffer().getProposedStartTime())) {
            throw new BadRequestException("Cannot start order before expert start time");
        }

        order.setOrderStatus(OrderStatus.STARTED);
        order.setActualStartTime(LocalDateTime.now());
        return customerOrderRepository.save(order);
    }

    @Override
    @Transactional
    public CustomerOrder completeOrder(Long orderId) {
        CustomerOrder order = findById(orderId);

        if (order.getOrderStatus() != OrderStatus.STARTED) {
            throw new BadRequestException("Order has not started yet");
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setActualEndTime(LocalDateTime.now());
        order.setOrderPaymentStatus(OrderPaymentStatus.UNPAID);
        return customerOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findByStatus(OrderStatus status, Pageable pageable) {
        return customerOrderRepository.findByOrderStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder>
    findAvailableOrdersForExpert(Long expertId, Pageable pageable) {
        return customerOrderRepository
                .findByHomeService_Experts_IdAndOrderStatusIn(expertId,
                        List.of(
                                OrderStatus.WAITING_FOR_OFFERS,
                                OrderStatus.WAITING_FOR_SELECTION
                        ),
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerOrder findCustomerOrder(Long customerId, Long orderId) {

        CustomerOrder order = findById(orderId);
        validateOwnership(order, customerId);
        return order;
    }

    private void validateOwnership(CustomerOrder order, Long customerId) {
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("Order does not belong to customer");
        }
    }


    @Override
    @Transactional
    public void markAsPaid(CustomerOrder order) {

        order.setOrderPaymentStatus(OrderPaymentStatus.PAID);

        customerOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findOrderHistory(Long expertId, Pageable pageable) {

        expertCoreService.findById(expertId);

        return customerOrderRepository.findHistoryByExpertId(expertId, pageable);
    }


    @Override
    public void validatePayOrder(CustomerOrder order, Long customerId) {

        validateOrderState(order, customerId);
        validatePaymentState(order);
        validateOrderPricing(order);
        validateExpertAssignment(order);
    }

    private void validateOrderState(CustomerOrder order, Long customerId) {

        if (Objects.isNull(order)) {
            throw new NotFoundException("ORDER_NOT_FOUND");
        }

        if (Objects.isNull(order.getCustomer())) {
            throw new BadRequestException("INVALID_ORDER_CUSTOMER");
        }

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("ORDER_NOT_BELONG_TO_CUSTOMER");
        }

        if (!OrderStatus.COMPLETED.equals(order.getOrderStatus())) {
            throw new BadRequestException("ORDER_NOT_COMPLETED");
        }
    }

    private void validatePaymentState(CustomerOrder order) {

        if (OrderPaymentStatus.PAID.equals(order.getOrderPaymentStatus())) {
            throw new BadRequestException("ALREADY_PAID");
        }
    }

    private void validateOrderPricing(CustomerOrder order) {

        if (Objects.isNull(order.getFinalPrice())
                || order.getFinalPrice().signum() <= 0) {

            throw new BadRequestException("INVALID_ORDER_PRICE");
        }
    }

    private void validateExpertAssignment(CustomerOrder order) {

        if (Objects.isNull(order.getAcceptedOffer())
                || Objects.isNull(order.getAcceptedOffer().getExpert())) {

            throw new BadRequestException("INVALID_EXPERT_ASSIGNMENT");
        }
    }

}
