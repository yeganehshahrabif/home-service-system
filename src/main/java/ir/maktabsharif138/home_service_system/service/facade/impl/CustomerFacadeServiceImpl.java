package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.entity.enums.SortBy;
import ir.maktabsharif138.home_service_system.mapper.CustomerMapper;
import ir.maktabsharif138.home_service_system.mapper.CustomerOrderMapper;
import ir.maktabsharif138.home_service_system.mapper.HomeServiceMapper;
import ir.maktabsharif138.home_service_system.mapper.OfferMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.HomeServiceCoreService;
import ir.maktabsharif138.home_service_system.service.core.OfferCoreService;
import ir.maktabsharif138.home_service_system.service.facade.CustomerFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFacadeServiceImpl implements CustomerFacadeService {

    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper customerOrderMapper;
    private final HomeServiceMapper homeServiceMapper;
    private final OfferMapper offerMapper;
    private final OfferCoreService offerCoreService;
    private final CustomerCoreService customerCoreService;
    private final HomeServiceCoreService homeServiceCoreService;
    private final CustomerOrderCoreService customerOrderCoreService;

    @Override
    public CustomerResponse register(CustomerRegisterRequest request) {
        Customer customer = customerMapper.toCustomer(request);
        Customer saved = customerCoreService.register(customer);
        return customerMapper.toCustomerResponse(saved);
    }

    @Override
    public LoginResponse login(CustomerLoginRequest request) {
        Customer customer = customerCoreService.login(request.getEmail(), request.getPassword());
        return customerMapper.toLoginResponse(customer);
    }

    @Override
    public CustomerResponse getProfile(Long id) {
        Customer customer = customerCoreService.findById(id);
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateProfile(Long id, CustomerUpdateRequest request) {

        Customer customer = customerCoreService.findById(id);
        customerCoreService.checkUpdate(customer, request);
        customerMapper.updateCustomer(customer, request);
        Customer saved = customerCoreService.update(customer);
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
    public CustomerOrderResponse createOrder(Long customerId, OrderCreateRequest request) {

        Customer customer = customerCoreService.findById(customerId);
        HomeService homeService = homeServiceCoreService.findById(request.getHomeServiceId());
        CustomerOrder order = customerOrderMapper.toCustomerOrder(request);
        order.setCustomer(customer);
        order.setHomeService(homeService);
        CustomerOrder saved = customerOrderCoreService.createOrder(order);

        return customerOrderMapper.toCustomerOrderResponse(saved);
    }

    @Override
    public List<CustomerOrderResponse> getMyOrders(Long customerId) {

        return customerOrderMapper.toOrderResponse(customerOrderCoreService
                .findByCustomerId(customerId));
    }

    @Override
    public CustomerOrderResponse startOrder(Long orderId) {
        CustomerOrder order = customerOrderCoreService.startOrder(orderId);
        return customerOrderMapper.toCustomerOrderResponse(order);
    }

    @Override
    public CustomerOrderResponse completeOrder(Long orderId) {

        CustomerOrder order = customerOrderCoreService.completeOrder(orderId);
        return customerOrderMapper.toCustomerOrderResponse(order);
    }

    @Override
    public List<OfferResponse> getOrderOffers(Long customerId, Long orderId, SortBy sortBy) {

        customerOrderCoreService.findCustomerOrder(customerId, orderId);

        List<Offer> offers =
                switch (sortBy) {
                    case PRICE -> offerCoreService.findByOrderIdSortedByPrice(orderId);

                    case RATING -> offerCoreService.findByOrderIdSortedByExpertRating(orderId);
                };

        return offerMapper.toOfferResponse(offers);
    }

    @Override
    @Transactional
    public OfferResponse acceptOffer(Long customerId, Long orderId, Long offerId) {
        customerOrderCoreService.findCustomerOrder(customerId, orderId);
        Offer offer = offerCoreService.acceptOffer(orderId, offerId);
        return offerMapper.toOfferResponse(offer);
    }

    @Override
    public ReviewResponse addReview(ReviewCreateRequest request) {
        return null;
    }
}
