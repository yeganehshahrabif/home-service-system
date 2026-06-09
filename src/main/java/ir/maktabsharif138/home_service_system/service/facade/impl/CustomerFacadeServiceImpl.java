package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.mapper.CustomerMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.facade.CustomerFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFacadeServiceImpl implements CustomerFacadeService {

    private final CustomerMapper customerMapper;
    private final CustomerCoreService customerCoreService;

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
        return List.of();
    }

    @Override
    public List<HomeServiceResponse> getSubServices(Long parentId) {
        return List.of();
    }

    @Override
    public CustomerOrderResponse createOrder(Long customerId, OrderCreateRequest request) {
        return null;
    }

    @Override
    public List<CustomerOrderResponse> getMyOrders(Long customerId) {
        return List.of();
    }

    @Override
    public List<OfferResponse> getOffersForOrder(Long orderId, String sortBy) {
        return List.of();
    }

    @Override
    public CustomerOrderResponse acceptOffer(Long orderId, Long offerId) {
        return null;
    }

    @Override
    public CustomerOrderResponse startOrder(Long orderId) {
        return null;
    }

    @Override
    public CustomerOrderResponse completeOrder(Long orderId) {
        return null;
    }

    @Override
    public ReviewResponse addReview(ReviewCreateRequest request) {
        return null;
    }
}
