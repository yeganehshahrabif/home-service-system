package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.DuplicateResourceException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertCoreServiceImpl implements ExpertCoreService {

    private final ExpertRepository expertRepository;
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
        expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        return expertRepository.save(expert);
    }

    @Override
    public Expert login(String email, String rawPassword) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, expert.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }
        if (AccountStatus.REJECTED.equals((expert.getAccountStatus()))) {
            throw new BadRequestException("Your account approval request has been rejected by admin");
        }
        if (!AccountStatus.APPROVED.equals(expert.getAccountStatus())) {
            throw new BadRequestException("Account not approved yet");
        }
        return expert;
    }

    @Override
    public void checkUpdate(Expert existing, ExpertUpdateRequest request) {

        if (hasActiveJob(existing.getId())) {
            throw new BadRequestException("Cannot update while having active job");
        }
        if (StringUtils.hasText(request.getEmail())) {
            if (request.getEmail().equals(existing.getEmail())) {
                throw new BadRequestException(
                        "This email is already your current email"
                );
            }
            if (expertRepository.existsByEmail(request.getEmail()) &&
                    !request.getEmail().equals(existing.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }
        }

    }

    private boolean hasActiveJob(Long expertId) {
        return customerOrderRepository.existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
                expertId, List.of(OrderStatus.STARTED, OrderStatus.WAITING_FOR_EXPERT));
    }

    @Override
    @Transactional
    @CacheEvict(value = "pendingExperts", allEntries = true)
    public Expert update(Expert expert) {

        if (StringUtils.hasText(expert.getPassword()) && !expert.getPassword().startsWith("$2a")) {
            expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        }
        expert.setAccountStatus(AccountStatus.PENDING_APPROVAL);

        return expertRepository.save(expert);
    }

    @Override
    public Expert findById(Long id) {
        return expertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expert not found "));
    }

    @Override
    public boolean existsByEmail(String email) {
        return expertRepository.existsByEmail(email);
    }

    @Override
    @Cacheable(value = "pendingExperts")
    public List<Expert> findPendingExperts() {
        return expertRepository.findByAccountStatus(AccountStatus.PENDING_APPROVAL);
    }

    @Override
    @Transactional
    @CacheEvict(value = "pendingExperts", allEntries = true)
    public void approveExpert(Long id) {
        Expert expert = findById(id);
        if (AccountStatus.REJECTED.equals(expert.getAccountStatus())) {
            throw new BadRequestException("Account already rejected");
        }
        expert.setAccountStatus(AccountStatus.APPROVED);
        expertRepository.save(expert);
    }

    @Override
    @Transactional
    @CacheEvict(value = "pendingExperts", allEntries = true)
    public void rejectExpert(Long id) {
        Expert expert = findById(id);
        if (AccountStatus.APPROVED.equals(expert.getAccountStatus())) {
            throw new BadRequestException("Account already approved");
        }
        expert.setAccountStatus(AccountStatus.REJECTED);
        expertRepository.save(expert);
    }

}

