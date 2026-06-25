package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.DuplicateResourceException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExpertCoreServiceImpl implements ExpertCoreService {

    private final ExpertRepository expertRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Expert register(Expert expert) {

        if (expertRepository.existsByEmail(expert.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        expert.setCreatedAt(LocalDateTime.now());
        expert.setRole(Role.EXPERT);
        expert.setRating(0D);
        expert.setReviewCount(0);
        expert.setPenaltyPoints(0);
        expert.setEmailVerified(false);
        expert.setAccountStatus(AccountStatus.NEW);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        expert.setWallet(wallet);
        return expertRepository.save(expert);
    }


    @Override
    @Transactional
    public void verifyEmail(Expert expert) {

        expert.setEmailVerified(true);
        if (isReadyForApproval(expert) && AccountStatus.NEW.equals(expert.getAccountStatus())) {
            expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        }
        expertRepository.save(expert);
    }

    private boolean isReadyForApproval(Expert expert) {
        return expert.isEmailVerified()
                && StringUtils.hasText(expert.getProfileImage());
    }

//    private void setInitialStatus(Expert expert) {
//        if (StringUtils.hasText(expert.getProfileImage())) {
//            expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);
//        } else {
//            expert.setAccountStatus(AccountStatus.NEW);
//        }
//    }

    @Override
    @Transactional(readOnly = true)
    public Expert login(String email, String rawPassword) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, expert.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }
        if (!expert.isEmailVerified()) {
            throw new BadRequestException("Email is not verified");
        }
        if (AccountStatus.REJECTED.equals(expert.getAccountStatus())) {
            throw new BadRequestException("Your account approval request has been rejected by admin");
        }
        if (!AccountStatus.APPROVED.equals(expert.getAccountStatus())) {
            throw new BadRequestException("Account not approved yet");
        }
        return expert;
    }

    @Override
    public void checkUpdate(Expert existing, ExpertUpdateRequest request, boolean hasImage) {

        if (AccountStatus.REJECTED.equals(existing.getAccountStatus())) {
            throw new BadRequestException("Rejected expert cannot update profile");
        }
        if (hasActiveJob(existing.getId())) {
            throw new BadRequestException("Cannot update while having active job");
        }
        if (isUpdateRequestEmpty(request, hasImage)) {
            throw new BadRequestException("No changes provided");
        }

        checkDuplicateEmail(existing, request);
    }

    private boolean hasActiveJob(Long expertId) {
        return customerOrderRepository.existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
                expertId, List.of(OrderStatus.STARTED, OrderStatus.WAITING_FOR_EXPERT));
    }

    private boolean isUpdateRequestEmpty(ExpertUpdateRequest request, boolean hasImage) {
        return Stream.of(
                StringUtils.hasText(request.getEmail()),
                StringUtils.hasText(request.getPassword()),
                hasImage
        ).noneMatch(Boolean::booleanValue);
    }

    private void checkDuplicateEmail(Expert existing, ExpertUpdateRequest request) {
        if (StringUtils.hasText(request.getEmail())
                && !request.getEmail().equals(existing.getEmail()) &&
                expertRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException("Email already exists");
        }
    }

    @Override
    @Transactional
    public Expert update(Expert expert) {

        encodePasswordIfNeeded(expert);
        updateStatusAfterProfileChange(expert);

        return expertRepository.save(expert);
    }

    private void updateStatusAfterProfileChange(Expert expert) {

        if (AccountStatus.APPROVED.equals(expert.getAccountStatus())) {
            expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);
            return;
        }

        if (AccountStatus.NEW.equals(expert.getAccountStatus())
                && isReadyForApproval(expert)) {

            expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        }
    }

    private void encodePasswordIfNeeded(Expert expert) {
        if (StringUtils.hasText(expert.getPassword()) && !expert.getPassword().startsWith("$2a")) {
            expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Expert findById(Long id) {
        return expertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expert not found "));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return expertRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Expert> findPendingExperts(Pageable pageable) {
        return expertRepository.findByAccountStatus(AccountStatus.PENDING_APPROVAL, pageable);
    }

    @Override
    @Transactional
    public void approveExpert(Long id) {
        Expert expert = findById(id);
        ensurePendingApprovalStatus(expert);
        if (!expert.isEmailVerified()) {
            throw new BadRequestException("Expert email must be verified before approval");
        }
        expert.setAccountStatus(AccountStatus.APPROVED);
        expertRepository.save(expert);
    }

    @Override
    @Transactional
    public void rejectExpert(Long id) {
        Expert expert = findById(id);
        ensurePendingApprovalStatus(expert);
        expert.setAccountStatus(AccountStatus.REJECTED);
        expertRepository.save(expert);
    }

    private void checkActivationStatus(Expert expert) {

        if (expert.getRating() < 0) {

            expert.setAccountStatus(AccountStatus.INACTIVE);

            expertRepository.save(expert);
        }
    }

    @Override
    @Transactional
    public void applyDelayPenalty(CustomerOrder order) {

        Offer offer = order.getAcceptedOffer();

        LocalDateTime expectedEnd = offer.getProposedStartTime().plusHours(offer.getDurationHours());
        LocalDateTime actualEnd = order.getActualEndTime();
        if (actualEnd == null || !actualEnd.isAfter(expectedEnd)) {
            return;
        }

        long delayHours = Duration.between(expectedEnd, actualEnd).toHours();

        Expert expert = offer.getExpert();
        int currentPenalty = expert.getPenaltyPoints() == null ? 0 : expert.getPenaltyPoints();
        expert.setPenaltyPoints(currentPenalty + (int) delayHours);

        recalculateRating(expert);

        checkActivationStatus(expert);
    }

    @Override
    @Transactional
    public void recalculateRating(Expert expert) {

        Double average = reviewRepository.findAverageRatingByExpertId(expert.getId());

        double reviewAverage = Objects.requireNonNullElse(average, 0.0);

        int penalty = expert.getPenaltyPoints() == null ? 0 : expert.getPenaltyPoints();
        expert.setRating(reviewAverage - penalty);

        expert.setReviewCount(
                Math.toIntExact(
                        reviewRepository.countByExpertId(expert.getId())
                )
        );
        expertRepository.save(expert);
    }

    @Override
    @Transactional(readOnly = true)
    public Expert findByEmail(String email) {

        return expertRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Expert not found"
                        )
                );
    }

    private void ensurePendingApprovalStatus(Expert expert) {

        if (!AccountStatus.PENDING_APPROVAL.equals(expert.getAccountStatus())) {
            throw new BadRequestException("Expert is not waiting for approval");
        }
    }

}

