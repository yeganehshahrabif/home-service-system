package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.enums.Role;

public interface EmailVerificationTokenCoreService {

    EmailVerificationToken createToken(String email, Role role);

    EmailVerificationToken findValidToken(String token);

    void markAsUsed(EmailVerificationToken token);

    void deleteExpiredTokens();
}