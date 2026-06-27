package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.dto.request.AdminHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.dto.request.OrderHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerOrderCoreService {

    CustomerOrder createOrder(CustomerOrder order);
    CustomerOrder findById(Long orderId);
    Page<CustomerOrder> findByCustomerId(Long customerId, Pageable pageable);
    CustomerOrder startOrder(Long orderId);
    CustomerOrder completeOrder(Long orderId);
    Page<CustomerOrder> findByStatus(OrderStatus status, Pageable pageable);
    Page<CustomerOrder> findAvailableOrdersForExpert(Long expertId, Pageable pageable);
    CustomerOrder findCustomerOrder(Long customerId,Long orderId );
    void validatePayOrder(CustomerOrder order, Long customerId);
    void markAsPaid(CustomerOrder order);
    Page<CustomerOrder> findOrderHistory(Long expertId, Pageable pageable);
    Page<CustomerOrder> getOrderHistory(Long customerId, OrderHistoryFilterRequest request, Pageable pageable);
    Page<CustomerOrder> getCustomerHistory(
            Long customerId,
            AdminHistoryFilterRequest request,
            Pageable pageable
    );

    Page<CustomerOrder> getExpertHistory(
            Long expertId,
            AdminHistoryFilterRequest request,
            Pageable pageable
    );

    CustomerOrder getOrderDetails(Long orderId);

}