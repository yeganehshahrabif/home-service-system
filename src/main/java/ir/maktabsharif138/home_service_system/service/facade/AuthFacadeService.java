package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.LoginRequest;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;

public interface AuthFacadeService {

    LoginResponse login(LoginRequest request);
}