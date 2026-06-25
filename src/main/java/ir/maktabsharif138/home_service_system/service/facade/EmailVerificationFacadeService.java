package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.response.EmailVerificationResponse;

public interface EmailVerificationFacadeService {

    EmailVerificationResponse verify(String token);
}
