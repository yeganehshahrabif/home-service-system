package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.dto.request.CustomerUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.Customer;

public interface CustomerCoreService {

    Customer register(Customer customer);
    Customer login(String email, String rawPassword);
    Customer findById(Long id);
    void checkUpdate(Customer customer, CustomerUpdateRequest request);
    Customer update(Customer customer);
    boolean existsByEmail(String email);
    Customer findByEmail(String email);
    void verifyEmail(Customer customer);
}