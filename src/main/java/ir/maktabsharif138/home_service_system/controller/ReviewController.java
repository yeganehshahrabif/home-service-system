package ir.maktabsharif138.home_service_system.controller;

import ir.maktabsharif138.home_service_system.dto.response.ReviewResponse;
import ir.maktabsharif138.home_service_system.service.facade.ReviewFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacadeService reviewFacadeService;

    @GetMapping("/expert/{expertId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByExpert(@PathVariable Long expertId) {
        // استفاده از ReviewService.getReviewsByExpertId(expertId)
        return null;
    }
}