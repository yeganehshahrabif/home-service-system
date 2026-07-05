package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.*;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewCoreServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ExpertCoreService expertCoreService;

    private ReviewCoreServiceImpl service;

    private Review review;
    private CustomerOrder order;
    private Expert expert;
    private Offer offer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {

        service = new ReviewCoreServiceImpl(reviewRepository, expertCoreService);

        pageable = PageRequest.of(0, 10);

        expert = new Expert();
        expert.setId(1L);

        offer = new Offer();
        offer.setId(100L);

        order = new CustomerOrder();
        order.setId(10L);
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderPaymentStatus(OrderPaymentStatus.PAID);
        order.setAcceptedOffer(offer);

        review = new Review();
        review.setId(50L);
        review.setExpert(expert);
        review.setCustomerOrder(order);
    }

    @Test
    void createReview_shouldSaveSuccessfully() {

        when(reviewRepository.existsByCustomerOrderId(10L))
                .thenReturn(false);

        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Review result = service.createReview(review);

        assertNotNull(result);
        assertEquals(10L, result.getCustomerOrder().getId());

        verify(reviewRepository).save(any(Review.class));
        verify(expertCoreService).recalculateRating(expert);
    }

    @Test
    void createReview_shouldThrow_whenOrderNotCompleted() {

        order.setOrderStatus(OrderStatus.CANCELLED);

        assertThrows(BadRequestException.class,
                () -> service.createReview(review));
    }

    @Test
    void createReview_shouldThrow_whenPaymentNotPaid() {

        order.setOrderPaymentStatus(OrderPaymentStatus.UNPAID);

        assertThrows(BadRequestException.class,
                () -> service.createReview(review));
    }

    @Test
    void createReview_shouldThrow_whenAlreadyReviewed() {

        when(reviewRepository.existsByCustomerOrderId(10L))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> service.createReview(review));
    }

    @Test
    void createReview_shouldThrow_whenNoAcceptedOffer() {

        order.setAcceptedOffer(null);

        assertThrows(BadRequestException.class,
                () -> service.createReview(review));
    }

    @Test
    void findByExpertId_shouldReturnPage() {

        when(reviewRepository.findByExpertId(1L, pageable))
                .thenReturn(Page.empty());

        Page<Review> result = service.findByExpertId(1L, pageable);

        assertNotNull(result);
        verify(reviewRepository).findByExpertId(1L, pageable);
    }

    @Test
    void findExpertOrderReview_shouldReturnReview() {

        when(reviewRepository
                .findByExpertIdAndCustomerOrderId(1L, 10L))
                .thenReturn(Optional.of(review));

        Review result =
                service.findExpertOrderReview(1L, 10L);

        assertNotNull(result);
        assertEquals(50L, result.getId());

        verify(expertCoreService)
                .validateApprovedExpert(1L);
    }
    @Test
    void findExpertOrderReview_shouldThrow_whenNotFound() {

        when(reviewRepository
                .findByExpertIdAndCustomerOrderId(1L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findExpertOrderReview(1L, 10L)
        );

        verify(expertCoreService)
                .validateApprovedExpert(1L);
    }

    @Test
    void findExpertOrderReview_shouldThrow_whenExpertNotApproved() {

        doThrow(
                new BadRequestException(
                        "Expert account is not approved yet"
                )
        ).when(expertCoreService)
                .validateApprovedExpert(1L);

        assertThrows(
                BadRequestException.class,
                () -> service.findExpertOrderReview(
                        1L,
                        10L
                )
        );

        verify(reviewRepository, never())
                .findByExpertIdAndCustomerOrderId(
                        anyLong(),
                        anyLong()
                );
    }
}