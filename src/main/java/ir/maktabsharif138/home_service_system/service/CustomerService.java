package ir.maktabsharif138.home_service_system.service;

import ir.maktabsharif138.home_service_system.dto.request.CustomerLoginRequest;
import ir.maktabsharif138.home_service_system.dto.request.CustomerRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.CustomerUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerResponse;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;

public interface CustomerService {

    CustomerResponse register(CustomerRegisterRequest request);
    LoginResponse login(CustomerLoginRequest request);
    CustomerResponse findById(Long id);
    CustomerResponse updateProfile(Long id, CustomerUpdateRequest request);
}
