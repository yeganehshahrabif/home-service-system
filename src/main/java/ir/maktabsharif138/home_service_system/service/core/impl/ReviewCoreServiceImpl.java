package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Review;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import ir.maktabsharif138.home_service_system.service.core.ReviewCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewCoreServiceImpl implements ReviewCoreService {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review createReview(Review review) {

        CustomerOrder order = review.getCustomerOrder();

        validateReview(order);

        review.setReviewDate(LocalDateTime.now());
        Review saved = reviewRepository.save(review);

        updateExpertRating(review.getExpert());
        return saved;
    }

    @Override
    public List<Review> findByExpertId(Long expertId) {
        return reviewRepository.findByExpertId(expertId);
    }

    private void validateReview(CustomerOrder order) {

        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new BadRequestException("Order is not completed");
        }

        if (reviewRepository.existsByCustomerOrderId(order.getId())) {
            throw new BadRequestException("Review already submitted");
        }

        if (Objects.isNull(order.getAcceptedOffer())) {
            throw new BadRequestException("Accepted offer not found");
        }
    }

    private void updateExpertRating(Expert expert) {

        Double average = reviewRepository.findAverageRatingByExpertId(expert.getId());
        expert.setRating(Objects.requireNonNullElse(average,0.0));
        expert.setReviewCount(
                Math.toIntExact(
                        reviewRepository.countByExpertId(expert.getId())
                )
        );
    }
}