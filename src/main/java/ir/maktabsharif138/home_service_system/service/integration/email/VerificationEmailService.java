package ir.maktabsharif138.home_service_system.service.integration.email;

import ir.maktabsharif138.home_service_system.entity.enums.Role;

public interface VerificationEmailService {

    void sendVerificationEmail(
            String email,
            Role role
    );
}