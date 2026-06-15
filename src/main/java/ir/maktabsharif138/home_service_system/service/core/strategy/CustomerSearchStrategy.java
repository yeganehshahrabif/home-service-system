package ir.maktabsharif138.home_service_system.service.core.strategy;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import ir.maktabsharif138.home_service_system.service.core.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerSearchStrategy implements UserSearchStrategy {

    private final CustomerRepository repository;

    @Override
    public boolean supports(Role role) {
        return Role.CUSTOMER.equals(role);
    }

    @Override
    public Page<Customer> search(UserSearchRequest request, Pageable pageable) {

        return repository.findAll(
                CustomerSpecification.filter(request),
                pageable
        );
    }
}