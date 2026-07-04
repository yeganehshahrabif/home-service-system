package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpertFacadeService {

    ExpertResponse register(ExpertRegisterRequest request,  MultipartFile image);
//    LoginResponse login(ExpertLoginRequest request);
    ExpertResponse getProfile();
    ExpertResponse updateProfile(ExpertUpdateRequest request, MultipartFile image);


    OfferResponse createOffer(OfferCreateRequest request);
    Page<OfferResponse> getMyOffers(Pageable pageable);


    Page<CustomerOrderResponse> getAvailableOrdersForExpert(Pageable pageable);
    Page<ExpertOrderHistoryResponse> findOrderHistory(Pageable pageable);

    ExpertOrderRatingResponse getOrderRating(Long orderId);
}