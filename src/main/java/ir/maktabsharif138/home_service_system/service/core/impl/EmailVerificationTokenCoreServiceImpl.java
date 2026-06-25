package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.EmailVerificationTokenRepository;
import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import ir.maktabsharif138.home_service_system.service.integration.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenCoreServiceImpl
        implements EmailVerificationTokenCoreService {

    private static final long TOKEN_EXPIRE_HOURS = 24;

    private final EmailVerificationTokenRepository repository;
    private final TokenGenerator tokenGenerator;

    @Override
    @Transactional
    public EmailVerificationToken createToken(String email, Role role) {

        repository.findByEmailAndRoleAndUsedFalse(email, role)
                .ifPresent(repository::delete);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken(tokenGenerator.generate());
        token.setEmail(email);
        token.setRole(role);
        token.setExpireAt(LocalDateTime.now().plusHours(TOKEN_EXPIRE_HOURS));
        token.setUsed(false);

        return repository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailVerificationToken findValidToken(String token) {

        EmailVerificationToken verificationToken =
                repository.findByToken(token)
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Verification token not found"
                                )
                        );

        if (verificationToken.isUsed()) {
            throw new BadRequestException("Verification link already used");
        }

        if (verificationToken.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification link expired");
        }

        return verificationToken;
    }

    @Override
    @Transactional
    public void markAsUsed(EmailVerificationToken token) {

        token.setUsed(true);

        repository.save(token);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {

        repository.deleteByExpireAtBefore(LocalDateTime.now());
    }
}