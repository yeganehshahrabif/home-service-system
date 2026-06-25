package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends
        JpaRepository<@NonNull EmailVerificationToken, @NonNull Long> {

    Optional<EmailVerificationToken> findByToken(String token);
}