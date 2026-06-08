package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Review;
import java.util.List;

public interface ReviewCoreService {

    Review createReview(Review review); // چک completed و عدم تکراری
    List<Review> findByExpertId(Long expertId);
    Double getAverageRatingForExpert(Long expertId);
}