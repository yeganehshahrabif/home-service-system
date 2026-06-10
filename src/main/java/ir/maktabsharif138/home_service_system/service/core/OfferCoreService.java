package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Offer;
import java.util.List;

public interface OfferCoreService {

    Offer createOffer(Offer offer);
    List<Offer> findByExpertId(Long expertId);
    List<Offer> findByOrderIdSortedByPrice(Long orderId);
    List<Offer> findByOrderIdSortedByExpertRating(Long orderId);
    Offer findById(Long id);
    Offer acceptOffer(Long orderId, Long offerId);
}