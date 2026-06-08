package ir.maktabsharif138.home_service_system.service;

import ir.maktabsharif138.home_service_system.dto.request.OfferCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.OfferResponse;
import java.util.List;

public interface OfferService {

    OfferResponse createOffer(OfferCreateRequest request);
    List<OfferResponse> getOffersByExpertId(Long expertId);
    List<OfferResponse> getOffersByOrderId(Long orderId);


    List<OfferResponse> getOffersByOrderSortedByPrice(Long orderId);
    List<OfferResponse> getOffersByOrderSortedByExpertRating(Long orderId);
    OfferResponse acceptOffer(Long orderId, Long offerId);
}
