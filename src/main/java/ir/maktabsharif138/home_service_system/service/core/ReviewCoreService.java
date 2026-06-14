package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReviewCoreService {

    Review createReview(Review review);
    Page<Review> findByExpertId(Long expertId, Pageable pageable);

}