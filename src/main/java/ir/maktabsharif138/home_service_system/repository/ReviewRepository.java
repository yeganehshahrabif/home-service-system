package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.Review;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<@NonNull Review,@NonNull Long> {

    List<Review> findByExpertId(Long expertId);

    boolean existsByCustomerOrderId(Long orderId);

    @Query("""
       select avg(r.rating)
       from Review r
       where r.expert.id = :expertId
       """)
    Double findAverageRatingByExpertId(Long expertId);

    long countByExpertId(Long expertId);
}
