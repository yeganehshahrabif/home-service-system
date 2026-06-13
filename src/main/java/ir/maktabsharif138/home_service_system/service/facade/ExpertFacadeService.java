package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpertFacadeService {

    ExpertResponse register(ExpertRegisterRequest request,  MultipartFile image);
    LoginResponse login(ExpertLoginRequest request);
    ExpertResponse getProfile(Long id);
    ExpertResponse updateProfile(Long id, ExpertUpdateRequest request, MultipartFile image);


    OfferResponse createOffer(Long expertId, OfferCreateRequest request);
    List<OfferResponse> getMyOffers(Long expertId);


    List<CustomerOrderResponse> getAvailableOrdersForExpert(Long expertId);
    List<CustomerOrderResponse> getOrderHistory(Long expertId);
}