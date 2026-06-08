package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.service.core.OfferCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferCoreServiceImpl implements OfferCoreService {
    @Override
    public Offer createOffer(Offer offer) {
        return null;
    }

    @Override
    public List<Offer> findByExpertId(Long expertId) {
        return List.of();
    }

    @Override
    public List<Offer> findByOrderIdSortedByPrice(Long orderId) {
        return List.of();
    }

    @Override
    public List<Offer> findByOrderIdSortedByExpertRating(Long orderId) {
        return List.of();
    }

    @Override
    public Offer findById(Long id) {
        return null;
    }

    @Override
    public Offer acceptOffer(Long orderId, Long offerId) {
        return null;
    }
}
