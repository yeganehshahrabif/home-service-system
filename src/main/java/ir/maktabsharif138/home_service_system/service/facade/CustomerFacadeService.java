package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.enums.SortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerFacadeService {

    CustomerResponse register(CustomerRegisterRequest request);

//    LoginResponse login(CustomerLoginRequest request);

    CustomerResponse getProfile();
    CustomerResponse updateProfile(CustomerUpdateRequest request);


    List<HomeServiceResponse> getAllMainServices();
    List<HomeServiceResponse> getSubServices(Long parentId);


    CustomerOrderResponse createOrder(OrderCreateRequest request);
    Page<CustomerOrderResponse> getMyOrders(Pageable pageable);
    CustomerOrderResponse startOrder(Long orderId);
    CustomerOrderResponse completeOrder(Long orderId);
    Page<CustomerOrderResponse> getOrderHistory(
            OrderHistoryFilterRequest request,
            Pageable pageable
    );


    Page<OfferResponse> getOrderOffers(Long orderId, SortBy sortBy, Pageable pageable);
    OfferResponse acceptOffer(Long orderId, Long offerId);


    ReviewResponse addReview(ReviewCreateRequest request);
}