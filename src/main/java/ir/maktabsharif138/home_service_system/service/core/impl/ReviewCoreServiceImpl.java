package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Review;
import ir.maktabsharif138.home_service_system.service.core.ReviewCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCoreServiceImpl implements ReviewCoreService {
    @Override
    public Review createReview(Review review) {
        return null;
    }

    @Override
    public List<Review> findByExpertId(Long expertId) {
        return List.of();
    }

    @Override
    public Double getAverageRatingForExpert(Long expertId) {
        return 0.0;
    }
}
