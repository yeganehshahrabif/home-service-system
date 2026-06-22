package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OfferCoreService {

    Offer createOffer(Offer offer);
    Page<Offer> findByExpertId(Long expertId, Pageable pageable);
    Page<Offer> findByOrderIdSortedByPrice(Long orderId, Pageable pageable);
    Page<Offer> findByOrderIdSortedByExpertRating(Long orderId, Pageable pageable);
    Offer findById(Long id);
    Offer acceptOffer(Long orderId, Long offerId);
}