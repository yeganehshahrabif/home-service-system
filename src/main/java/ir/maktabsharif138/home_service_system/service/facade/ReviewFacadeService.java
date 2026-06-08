package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewFacadeService {

    List<ReviewResponse> getReviewsByExpert(Long expertId);
}