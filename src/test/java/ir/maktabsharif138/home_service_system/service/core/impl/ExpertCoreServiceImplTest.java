package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.DuplicateResourceException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpertCoreServiceImplTest {

    @Mock
    private ExpertRepository expertRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CustomerOrderRepository customerOrderRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ExpertCoreServiceImpl service;

    private Expert expert;

    @BeforeEach
    void setUp() {
        expert = new Expert();
        expert.setId(1L);
        expert.setEmail("test@gmail.com");
        expert.setPassword("123");
        expert.setHomeServices(new HashSet<>());
        expert.setPenaltyPoints(0);
        expert.setRating(0D);
    }

    @Test
    void register_shouldSetNewStatus() {

        when(passwordEncoder.encode("123"))
                .thenReturn("encoded");

        when(expertRepository.existsByEmail(any()))
                .thenReturn(false);

        when(expertRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Expert result = service.register(expert);

        assertEquals(AccountStatus.NEW, result.getAccountStatus());
        assertEquals(Role.EXPERT, result.getRole());

        assertFalse(result.isEmailVerified());

        assertNotNull(result.getWallet());

        verify(expertRepository).save(any());
    }

    @Test
    void register_shouldSetNew_whenNoImage() {

        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(expertRepository.existsByEmail(any())).thenReturn(false);
        when(expertRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Expert result = service.register(expert);

        assertEquals(AccountStatus.NEW, result.getAccountStatus());
    }

    @Test
    void register_shouldThrow_whenEmailExists() {

        when(expertRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.register(expert)
        );
    }

    @Test
    void register_shouldCreateWallet() {

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(expertRepository.existsByEmail(any()))
                .thenReturn(false);

        when(expertRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Expert result = service.register(expert);

        assertNotNull(result.getWallet());
        assertEquals(
                BigDecimal.ZERO,
                result.getWallet().getBalance()
        );
    }


    @Test
    void checkUpdate_shouldThrow_whenRejected() {

        expert.setAccountStatus(AccountStatus.REJECTED);

        assertThrows(
                BadRequestException.class,
                () -> service.checkUpdate(
                        expert,
                        new ExpertUpdateRequest(),
                        false
                )
        );
    }

    @Test
    void checkUpdate_shouldThrow_whenActiveJobExists() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        when(customerOrderRepository
                .existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
                        eq(1L),
                        any()))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> service.checkUpdate(
                        expert,
                        new ExpertUpdateRequest(),
                        false
                )
        );
    }

    @Test
    void checkUpdate_shouldThrow_whenNoChangesProvided() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        when(customerOrderRepository
                .existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
                        eq(1L),
                        any()))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.checkUpdate(
                        expert,
                        new ExpertUpdateRequest(),
                        false
                )
        );
    }

    @Test
    void checkUpdate_shouldThrow_whenDuplicateEmail() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        ExpertUpdateRequest request =
                new ExpertUpdateRequest();
        request.setEmail("new@gmail.com");

        when(customerOrderRepository
                .existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
                        eq(1L),
                        any()))
                .thenReturn(false);

        when(expertRepository.existsByEmail("new@gmail.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.checkUpdate(expert, request, false)
        );
    }

    @Test
    void checkUpdate_shouldPassSuccessfully() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        ExpertUpdateRequest request =
                new ExpertUpdateRequest();
        request.setEmail("new@gmail.com");

        when(customerOrderRepository
                .existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
                        eq(1L),
                        any()))
                .thenReturn(false);

        when(expertRepository.existsByEmail("new@gmail.com"))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> service.checkUpdate(
                        expert,
                        request,
                        false
                )
        );
    }

    @Test
    void update_shouldNotEncodePassword_whenAlreadyEncoded() {

        expert.setPassword("$2aHash");
        expert.setAccountStatus(AccountStatus.NEW);

        when(expertRepository.save(any()))
                .thenReturn(expert);

        service.update(expert);

        verify(passwordEncoder, never())
                .encode(any());
    }

    @Test
    void update_shouldKeepStatusNew_whenNoImage() {

        expert.setPassword("$2aHash");
        expert.setAccountStatus(AccountStatus.NEW);
        expert.setProfileImage(null);

        when(expertRepository.save(any()))
                .thenReturn(expert);

        Expert result = service.update(expert);

        assertEquals(
                AccountStatus.NEW,
                result.getAccountStatus()
        );
    }

    @Test
    void update_shouldEncodePassword() {

        expert.setPassword("raw");
        expert.setAccountStatus(AccountStatus.NEW);
        expert.setProfileImage("img");

        when(passwordEncoder.encode("raw"))
                .thenReturn("encoded");

        when(expertRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Expert result = service.update(expert);

        assertEquals("encoded", result.getPassword());

    }

    @Test
    void update_shouldSetPendingApproval_whenApprovedExpertChanges() {

        expert.setPassword("$2aHash");
        expert.setAccountStatus(AccountStatus.APPROVED);

        when(expertRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Expert result = service.update(expert);

        assertEquals(AccountStatus.PENDING_APPROVAL,
                result.getAccountStatus());
    }

    @Test
    void update_shouldSetPendingApproval_whenReadyForApproval() {

        expert.setAccountStatus(AccountStatus.NEW);
        expert.setEmailVerified(true);
        expert.setProfileImage("img");
        expert.setPassword("$2aHash");
        when(expertRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));
        Expert result = service.update(expert);
        assertEquals(
                AccountStatus.PENDING_APPROVAL,
                result.getAccountStatus()
        );
    }

    @Test
    void update_shouldKeepPending_whenAlreadyPending() {

        expert.setAccountStatus(
                AccountStatus.PENDING_APPROVAL
        );

        expert.setPassword("$2aHash");

        when(expertRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Expert result = service.update(expert);

        assertEquals(
                AccountStatus.PENDING_APPROVAL,
                result.getAccountStatus()
        );
    }

    @Test
    void verifyEmail_shouldSetPendingApproval() {

        expert.setAccountStatus(AccountStatus.NEW);
        expert.setProfileImage("image");
        service.verifyEmail(expert);
        assertTrue(expert.isEmailVerified());
        assertEquals(
                AccountStatus.PENDING_APPROVAL,
                expert.getAccountStatus()
        );
        verify(expertRepository).save(expert);
    }
    @Test
    void verifyEmail_shouldRemainNew_whenNoImage() {

        expert.setAccountStatus(AccountStatus.NEW);
        expert.setProfileImage(null);
        service.verifyEmail(expert);
        assertTrue(expert.isEmailVerified());
        assertEquals(
                AccountStatus.NEW,
                expert.getAccountStatus()
        );
    }

    @Test
    void verifyEmail_shouldRemainPending_whenAlreadyPending() {

        expert.setAccountStatus(
                AccountStatus.PENDING_APPROVAL
        );

        expert.setProfileImage("image");

        service.verifyEmail(expert);

        assertEquals(
                AccountStatus.PENDING_APPROVAL,
                expert.getAccountStatus()
        );

        assertTrue(expert.isEmailVerified());
    }

    @Test
    void approveExpert_shouldApprove() {

        expert.setEmailVerified(true);
        expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);

        when(expertRepository.findById(1L)).thenReturn(Optional.of(expert));

        service.approveExpert(1L);
        assertEquals(
                AccountStatus.APPROVED,
                expert.getAccountStatus()
        );
        verify(expertRepository).save(expert);
    }

    @Test
    void approveExpert_shouldThrow_whenEmailNotVerified() {

        expert.setEmailVerified(false);
        expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        assertThrows(
                BadRequestException.class,
                () -> service.approveExpert(1L)
        );
    }

    @Test
    void approveExpert_shouldThrow_whenExpertNotFound() {

        when(expertRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.approveExpert(1L)
        );
    }

    @Test
    void rejectExpert_shouldReject() {

        expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        service.rejectExpert(1L);

        assertEquals(AccountStatus.REJECTED,
                expert.getAccountStatus());
    }

    @Test
    void rejectExpert_shouldThrow_whenNotPending() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        assertThrows(
                BadRequestException.class,
                () -> service.rejectExpert(1L)
        );
    }

    @Test
    void rejectExpert_shouldThrow_whenExpertNotFound() {

        when(expertRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.rejectExpert(1L)
        );
    }

    @Test
    void approveExpert_shouldThrow_whenNotPending() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        assertThrows(
                BadRequestException.class,
                () -> service.approveExpert(1L)
        );
    }

    @Test
    void recalculateRating_shouldUpdateValues() {

        when(reviewRepository.findAverageRatingByExpertId(1L))
                .thenReturn(4.5);

        when(reviewRepository.countByExpertId(1L))
                .thenReturn(10L);

        service.recalculateRating(expert);

        assertEquals(4.5, expert.getRating());
        assertEquals(10, expert.getReviewCount());
    }

    @Test
    void recalculateRating_shouldHandleNullAverage() {

        when(reviewRepository.findAverageRatingByExpertId(1L))
                .thenReturn(null);

        when(reviewRepository.countByExpertId(1L))
                .thenReturn(0L);

        service.recalculateRating(expert);

        assertEquals(0.0, expert.getRating());
    }

    @Test
    void recalculateRating_shouldSubtractPenalty() {

        expert.setPenaltyPoints(2);

        when(reviewRepository
                .findAverageRatingByExpertId(1L))
                .thenReturn(5.0);

        when(reviewRepository
                .countByExpertId(1L))
                .thenReturn(3L);

        service.recalculateRating(expert);

        assertEquals(
                3.0,
                expert.getRating()
        );
    }

    @Test
    void applyDelayPenalty_shouldIncreasePenalty() {

        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setDurationHours(2);
        offer.setProposedStartTime(
                LocalDateTime.now().minusHours(5)
        );

        CustomerOrder order = new CustomerOrder();
        order.setAcceptedOffer(offer);
        order.setActualEndTime(LocalDateTime.now());

        when(reviewRepository.findAverageRatingByExpertId(1L))
                .thenReturn(5.0);

        when(reviewRepository.countByExpertId(1L))
                .thenReturn(1L);

        service.applyDelayPenalty(order);

        assertTrue(expert.getPenaltyPoints() > 0);
    }

    @Test
    void applyDelayPenalty_shouldReturn_whenActualEndNull() {

        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setDurationHours(2);
        offer.setProposedStartTime(LocalDateTime.now());

        CustomerOrder order = new CustomerOrder();
        order.setAcceptedOffer(offer);
        order.setActualEndTime(null);

        service.applyDelayPenalty(order);

        assertEquals(0, expert.getPenaltyPoints());
    }

    @Test
    void applyDelayPenalty_shouldReturn_whenNoDelay() {

        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setDurationHours(2);
        offer.setProposedStartTime(
                LocalDateTime.now()
        );

        CustomerOrder order =
                new CustomerOrder();

        order.setAcceptedOffer(offer);
        order.setActualEndTime(
                LocalDateTime.now()
        );

        service.applyDelayPenalty(order);

        assertEquals(
                0,
                expert.getPenaltyPoints()
        );
    }

    @Test
    void applyDelayPenalty_shouldDeactivateExpert() {

        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setDurationHours(1);
        offer.setProposedStartTime(
                LocalDateTime.now()
                        .minusHours(10)
        );

        CustomerOrder order =
                new CustomerOrder();

        order.setAcceptedOffer(offer);
        order.setActualEndTime(
                LocalDateTime.now()
        );

        when(reviewRepository
                .findAverageRatingByExpertId(1L))
                .thenReturn(0.0);

        when(reviewRepository
                .countByExpertId(1L))
                .thenReturn(1L);

        service.applyDelayPenalty(order);

        assertEquals(
                AccountStatus.INACTIVE,
                expert.getAccountStatus()
        );

        verify(expertRepository, atLeastOnce())
                .save(expert);
    }

    @Test
    void existsByEmail_shouldReturnValue() {

        when(expertRepository.existsByEmail("a"))
                .thenReturn(true);

        assertTrue(service.existsByEmail("a"));
    }

    @Test
    void existsByEmail_shouldReturnFalse() {

        when(expertRepository.existsByEmail("a"))
                .thenReturn(false);

        assertFalse(service.existsByEmail("a"));
    }

    @Test
    void findByEmail_shouldReturnExpert() {

        when(expertRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(expert));

        Expert result =
                service.findByEmail("test@gmail.com");

        assertEquals(expert, result);
    }

    @Test
    void findByEmail_shouldThrowNotFoundException() {

        when(expertRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findByEmail("a@gmail.com")
        );
    }
    @Test
    void findPendingExperts_shouldReturnPage() {

        Page<Expert> page = Page.empty();

        when(expertRepository.findByAccountStatus(
                AccountStatus.PENDING_APPROVAL,
                Pageable.unpaged()))
                .thenReturn(page);

        assertEquals(page, service.findPendingExperts(Pageable.unpaged()));
    }

    @Test
    void findById_shouldReturnExpert() {

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        Expert result = service.findById(1L);

        assertEquals(expert, result);
    }

    @Test
    void validateApprovedExpert_shouldPass() {

        expert.setAccountStatus(AccountStatus.APPROVED);

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        assertDoesNotThrow(
                () -> service.validateApprovedExpert(1L)
        );
    }

    @Test
    void validateApprovedExpert_shouldThrow_whenNotApproved() {

        expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);

        when(expertRepository.findById(1L))
                .thenReturn(Optional.of(expert));

        assertThrows(
                BadRequestException.class,
                () -> service.validateApprovedExpert(1L)
        );
    }

    @Test
    void validateApprovedExpert_shouldThrow_whenExpertNotFound() {

        when(expertRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.validateApprovedExpert(1L)
        );
    }

}