package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import java.util.List;

public interface CustomerFacadeService {

    CustomerResponse register(CustomerRegisterRequest request);
    LoginResponse login(CustomerLoginRequest request);
    CustomerResponse getProfile(Long id);
    CustomerResponse updateProfile(Long id, CustomerUpdateRequest request);


    List<HomeServiceResponse> getAllMainServices();
    List<HomeServiceResponse> getSubServices(Long parentId);


    CustomerOrderResponse createOrder(Long customerId, OrderCreateRequest request);
    List<CustomerOrderResponse> getMyOrders(Long customerId);
    CustomerOrderResponse startOrder(Long orderId);
    CustomerOrderResponse completeOrder(Long orderId);

    List<OfferResponse> getOffersForOrder(Long orderId, String sortBy);
    CustomerOrderResponse acceptOffer(Long orderId, Long offerId);


    ReviewResponse addReview(ReviewCreateRequest request);
}