package ir.maktabsharif138.home_service_system.service;

import ir.maktabsharif138.home_service_system.dto.request.ReviewCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(ReviewCreateRequest request);
    List<ReviewResponse> getReviewsByExpertId(Long expertId);
    Double getAverageRatingForExpert(Long expertId);
}
