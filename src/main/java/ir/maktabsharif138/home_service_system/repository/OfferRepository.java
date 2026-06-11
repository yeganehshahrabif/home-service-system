package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.Offer;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<@NonNull Offer, @NonNull Long> {

    List<Offer> findByCustomerOrderId(Long orderId);

    List<Offer> findByExpertId(Long expertId);

    List<Offer> findByCustomerOrderIdOrderByProposedPriceAsc(Long orderId);

    boolean existsByCustomerOrderId(Long customerOrderId);

    boolean existsByCustomerOrderIdAndExpertId(Long orderId, Long expertId);

    @Query("""
    select o
    from Offer o
    left join o.expert e
    where o.customerOrder.id = :orderId
    order by coalesce(e.rating, 0) desc
""")
    List<Offer> findByOrderIdOrderByExpertRating(Long orderId);
}
