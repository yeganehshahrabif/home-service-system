package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.Offer;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<@NonNull Offer,@NonNull Long> {

    List<Offer> findByCustomerOrderId(Long orderId);

    List<Offer> findByExpertId(Long expertId);

    // برای مرتب‌سازی بر اساس قیمت (فاز دو)
    List<Offer> findByCustomerOrderIdOrderByProposedPriceAsc(Long orderId);
}
