package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Offer;
import java.util.List;

public interface OfferCoreService {

    Offer createOffer(Offer offer); // چک وضعیت سفارش و تخصص متخصص + تغییر وضعیت سفارش در صورت اولین پیشنهاد
    List<Offer> findByExpertId(Long expertId);
    List<Offer> findByOrderIdSortedByPrice(Long orderId);
    List<Offer> findByOrderIdSortedByExpertRating(Long orderId);
    Offer findById(Long id);
    Offer acceptOffer(Long orderId, Long offerId); // تغییر وضعیت سفارش به WAITING_FOR_EXPERT و رد بقیه
}