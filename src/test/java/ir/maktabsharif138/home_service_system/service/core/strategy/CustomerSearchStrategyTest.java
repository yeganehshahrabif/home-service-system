package ir.maktabsharif138.home_service_system.service.core.strategy;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSearchStrategyTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerSearchStrategy strategy;

    private UserSearchRequest request;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        request = new UserSearchRequest();
        pageable = Pageable.unpaged();
    }

    @Test
    void supports_shouldReturnTrue_whenRoleIsCustomer() {

        assertTrue(strategy.supports(Role.CUSTOMER));
    }

    @Test
    void supports_shouldReturnFalse_whenRoleIsNotCustomer() {

        assertFalse(strategy.supports(Role.EXPERT));
    }

    @Test
    void search_shouldReturnPageOfCustomers() {

        Customer customer = new Customer();
        customer.setId(1L);

        Page<Customer> expected = new PageImpl<>(List.of(customer));

        when(repository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(expected);

        Page<Customer> result = strategy.search(request, pageable);

        assertEquals(expected, result);

        verify(repository, times(1))
                .findAll(any(Specification.class), eq(pageable));
    }
}