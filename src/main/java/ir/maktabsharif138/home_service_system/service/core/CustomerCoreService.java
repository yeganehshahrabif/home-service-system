package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Customer;

public interface CustomerCoreService {

    Customer register(Customer customer);
    Customer login(String email, String rawPassword);
    Customer findById(Long id);
    Customer update(Customer customer);
}