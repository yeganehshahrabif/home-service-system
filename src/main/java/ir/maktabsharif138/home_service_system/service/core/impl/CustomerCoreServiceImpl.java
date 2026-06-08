package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCoreServiceImpl implements CustomerCoreService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Customer register(Customer customer) {
        return null;
    }

    @Override
    public Customer login(String email, String rawPassword) {
        return null;
    }

    @Override
    public Customer findById(Long id) {
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }
}
