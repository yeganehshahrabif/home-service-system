package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.entity.enums.SortBy;
import ir.maktabsharif138.home_service_system.mapper.*;
import ir.maktabsharif138.home_service_system.security.CurrentUserService;
import ir.maktabsharif138.home_service_system.service.core.*;
import ir.maktabsharif138.home_service_system.service.facade.CustomerFacadeService;
import ir.maktabsharif138.home_service_system.service.integration.email.VerificationEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFacadeServiceImpl implements CustomerFacadeService {

    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper customerOrderMapper;
    private final HomeServiceMapper homeServiceMapper;
    private final OfferMapper offerMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewCoreService reviewCoreService;
    private final OfferCoreService offerCoreService;
    private final CurrentUserService currentUserService;
    private final CustomerCoreService customerCoreService;
    private final HomeServiceCoreService homeServiceCoreService;
    private final CustomerOrderCoreService customerOrderCoreService;
    private final VerificationEmailService verificationEmailService;

    @Override
    public CustomerResponse register(CustomerRegisterRequest request) {
        Customer customer = customerMapper.toCustomer(request);
        Customer saved = customerCoreService.register(customer);
        verificationEmailService.sendVerificationEmail(saved.getEmail(), Role.CUSTOMER);
        return customerMapper.toCustomerResponse(saved);
    }

//    @Override
//    public LoginResponse login(CustomerLoginRequest request) {
//        Customer customer = customerCoreService.login(request.getEmail(), request.getPassword());
//        return customerMapper.toLoginResponse(customer);
//    }

    @Override
    public CustomerResponse getProfile() {
        Customer customer = getCurrentCustomer();
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateProfile(CustomerUpdateRequest request) {

        Customer customer = getCurrentCustomer();
        customerCoreService.checkUpdate(customer, request);
        String oldEmail = customer.getEmail();
        customerMapper.updateCustomer(customer, request);
        boolean emailChanged = StringUtils.hasText(request.getEmail())
                && !request.getEmail().equals(oldEmail);
        if (emailChanged) {
            customer.setEmailVerified(false);
        }
        Customer saved = customerCoreService.update(customer);
        if (emailChanged) {
            verificationEmailService.sendVerificationEmail(saved.getEmail(), Role.CUSTOMER);
        }
        return customerMapper.toCustomerResponse(saved);
    }


    @Override
    public List<HomeServiceResponse> getAllMainServices() {
        return homeServiceMapper.toHomeServiceResponse(homeServiceCoreService.findAllMainServices());
    }

    @Override
    public List<HomeServiceResponse> getSubServices(Long parentId) {
        return homeServiceMapper.toHomeServiceResponse(homeServiceCoreService
                .findSubServicesByParentId(parentId));
    }

    @Override
    @Transactional
    public CustomerOrderResponse createOrder(OrderCreateRequest request) {

        Customer customer = getCurrentCustomer();
        HomeService homeService = homeServiceCoreService.findById(request.getHomeServiceId());
        CustomerOrder order = customerOrderMapper.toCustomerOrder(request);
        order.setCustomer(customer);
        order.setHomeService(homeService);
        CustomerOrder saved = customerOrderCoreService.createOrder(order);

        return customerOrderMapper.toCustomerOrderResponse(saved);
    }

    @Override
    public CustomerOrderResponse startOrder(Long orderId) {
        CustomerOrder order = getCurrentCustomerOrder(orderId);
        CustomerOrder startedOrder = customerOrderCoreService.startOrder(order.getId());
        return customerOrderMapper.toCustomerOrderResponse(startedOrder);
    }

    @Override
    public CustomerOrderResponse completeOrder(Long orderId) {
        CustomerOrder order = getCurrentCustomerOrder(orderId);
        CustomerOrder completedOrder = customerOrderCoreService.completeOrder(order.getId());
        return customerOrderMapper.toCustomerOrderResponse(completedOrder);
    }

    @Override
    public Page<CustomerOrderResponse> getOrderHistory(
            OrderHistoryFilterRequest request,
            Pageable pageable
    ) {

        return customerOrderCoreService
                .getOrderHistory(getCurrentCustomerId(), request, pageable)
                .map(customerOrderMapper::toCustomerOrderResponse);
    }

    private Customer getCurrentCustomer() {

        return customerCoreService.findById(
                currentUserService.getCurrentUserId()
        );
    }

    @Override
    public Page<OfferResponse> getOrderOffers(Long orderId, SortBy sortBy, Pageable pageable) {

        getCurrentCustomerOrder(orderId);

        Page<Offer> offers =
                switch (sortBy) {
                    case PRICE -> offerCoreService.findByOrderIdSortedByPrice(orderId, pageable);

                    case RATING -> offerCoreService.findByOrderIdSortedByExpertRating(orderId, pageable);
                };

        return offers.map(offerMapper::toOfferResponse);
    }

    @Override
    @Transactional
    public OfferResponse acceptOffer(Long orderId, Long offerId) {
        getCurrentCustomerOrder(orderId);
        Offer offer = offerCoreService.acceptOffer(orderId, offerId);
        return offerMapper.toOfferResponse(offer);
    }

    @Override
    @Transactional
    public ReviewResponse addReview(ReviewCreateRequest request) {

        CustomerOrder order = getCurrentCustomerOrder(request.getOrderId());
        Review review = reviewMapper.toReview(request);
        review.setCustomer(order.getCustomer());
        review.setCustomerOrder(order);
        review.setExpert(order.getAcceptedOffer().getExpert());
        Review saved = reviewCoreService.createReview(review);

        return reviewMapper.toResponse(saved);
    }

    private CustomerOrder getCurrentCustomerOrder(Long orderId) {
        return customerOrderCoreService.findCustomerOrder(
                getCurrentCustomerId(),
                orderId
        );
    }

    private Long getCurrentCustomerId() {
        return currentUserService.getCurrentUserId();
    }

}
