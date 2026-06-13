package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.CustomerUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.DuplicateResourceException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerCoreServiceImpl implements CustomerCoreService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Customer register(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreatedAt(LocalDateTime.now());
        customer.setRole(Role.CUSTOMER);
        customer.setAccountStatus(AccountStatus.APPROVED);
        return customerRepository.save(customer);
    }

    @Override
    public Customer login(String email, String rawPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }
        return customer;
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    @Override
    public void checkUpdate(Customer existing, CustomerUpdateRequest request) {
        checkDuplicateEmail(existing, request);
    }

    private void checkDuplicateEmail(Customer existing, CustomerUpdateRequest request) {

        if (StringUtils.hasText(request.getEmail())
                && !request.getEmail().equals(existing.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

    }

    @Override
    @Transactional
    public Customer update(Customer customer) {

        encodePasswordIfNeeded(customer);

        return customerRepository.save(customer);
    }

    private void encodePasswordIfNeeded(Customer customer) {
        if (StringUtils.hasText(customer.getPassword()) && !customer.getPassword().startsWith("$2a")) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}