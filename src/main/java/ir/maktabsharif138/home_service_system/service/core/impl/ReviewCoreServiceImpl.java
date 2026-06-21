package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Review;
import ir.maktabsharif138.home_service_system.entity.enums.OrderPaymentStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.core.ReviewCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewCoreServiceImpl implements ReviewCoreService {

    private final ReviewRepository reviewRepository;
    private final ExpertRepository expertRepository;
    private final ExpertCoreService expertCoreService;

    @Override
    @Transactional
    public Review createReview(Review review) {

        CustomerOrder order = review.getCustomerOrder();

        validateReview(order);

        review.setReviewDate(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        expertCoreService.recalculateRating(review.getExpert());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> findByExpertId(Long expertId, Pageable pageable) {
        return reviewRepository.findByExpertId(expertId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Review findExpertOrderReview(Long expertId, Long orderId) {

        return reviewRepository
                .findByExpertIdAndCustomerOrderId(expertId, orderId)
                .orElseThrow(() ->
                        new NotFoundException("Rating not found"));
    }


    private void validateReview(CustomerOrder order) {

        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new BadRequestException("Order is not completed");
        }

        if (order.getOrderPaymentStatus() != OrderPaymentStatus.PAID) {
            throw new BadRequestException("Order payment not completed");
        }

        if (reviewRepository.existsByCustomerOrderId(order.getId())) {
            throw new BadRequestException("Review already submitted");
        }

        if (Objects.isNull(order.getAcceptedOffer())) {
            throw new BadRequestException("Accepted offer not found");
        }
    }
}