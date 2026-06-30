package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.CustomerUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.DuplicateResourceException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCoreServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerCoreServiceImpl service;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@mail.com");
        customer.setPassword("123456");
        customer.setEmailVerified(true);
    }

    @Test
    void register_shouldSaveCustomerSuccessfully() {

        when(customerRepository.existsByEmail(customer.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = service.register(customer);
        assertEquals("encodedPassword", result.getPassword());

        assertNotNull(result);
        assertEquals(Role.CUSTOMER, result.getRole());
        assertEquals(AccountStatus.APPROVED, result.getAccountStatus());
        assertFalse(result.isEmailVerified());
        assertNotNull(result.getWallet());
        assertEquals(BigDecimal.ZERO, result.getWallet().getBalance());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void register_shouldThrowDuplicateException_whenEmailExists() {

        when(customerRepository.existsByEmail(customer.getEmail()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.register(customer)
        );

        verify(customerRepository, never()).save(any());
    }

//    @Test
//    void login_shouldReturnCustomer_whenCredentialsCorrect() {
//
//        customer.setEmailVerified(true);
//
//        when(customerRepository.findByEmail(customer.getEmail()))
//                .thenReturn(Optional.of(customer));
//
//        when(passwordEncoder.matches(anyString(), anyString()))
//                .thenReturn(true);
//
//        Customer result = service.login(customer.getEmail(), "123");
//
//        assertNotNull(result);
//        assertEquals(customer.getEmail(), result.getEmail());
//    }
//
//    @Test
//    void login_shouldThrow_whenEmailNotVerified() {
//
//        customer.setEmailVerified(false);
//
//        when(customerRepository.findByEmail(customer.getEmail()))
//                .thenReturn(Optional.of(customer));
//
//        when(passwordEncoder.matches(anyString(), anyString()))
//                .thenReturn(true);
//
//        assertThrows(
//                BadRequestException.class,
//                () -> service.login(customer.getEmail(), "123")
//        );
//    }
//
//    @Test
//    void login_shouldThrowException_whenCustomerNotFound() {
//
//        when(customerRepository.findByEmail(anyString()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(
//                BadRequestException.class,
//                () -> service.login("x@mail.com", "123")
//        );
//    }
//
//    @Test
//    void login_shouldThrowException_whenPasswordWrong() {
//
//        when(customerRepository.findByEmail(customer.getEmail()))
//                .thenReturn(Optional.of(customer));
//
//        when(passwordEncoder.matches(anyString(), anyString()))
//                .thenReturn(false);
//
//        assertThrows(
//                BadRequestException.class,
//                () -> service.login(customer.getEmail(), "wrong")
//        );
//    }

    @Test
    void findById_shouldReturnCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer result = service.findById(1L);

        assertNotNull(result);
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    void findById_shouldThrowNotFoundException() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findById(1L)
        );
    }

    @Test
    void findByEmail_shouldReturnCustomer() {

        when(customerRepository.findByEmail(customer.getEmail()))
                .thenReturn(Optional.of(customer));

        Customer result =
                service.findByEmail(customer.getEmail());

        assertEquals(customer, result);
    }

    @Test
    void findByEmail_shouldThrow_whenNotFound() {

        when(customerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findByEmail("x@mail.com")
        );
    }

    @Test
    void checkUpdate_shouldThrow_whenRequestEmpty() {

        CustomerUpdateRequest request =
                new CustomerUpdateRequest();

        assertThrows(
                BadRequestException.class,
                () -> service.checkUpdate(customer, request)
        );
    }

    @Test
    void checkUpdate_shouldThrow_whenDuplicateEmail() {

        CustomerUpdateRequest request =
                new CustomerUpdateRequest();

        request.setEmail("new@mail.com");

        when(customerRepository.existsByEmail("new@mail.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.checkUpdate(customer, request)
        );
    }

    @Test
    void checkUpdate_shouldPass_whenValidRequest() {

        CustomerUpdateRequest request = new CustomerUpdateRequest();

        request.setEmail("new@mail.com");

        when(customerRepository.existsByEmail("new@mail.com"))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> service.checkUpdate(customer, request)
        );
    }

    @Test
    void checkUpdate_shouldPass_whenEmailUnchanged() {

        CustomerUpdateRequest request = new CustomerUpdateRequest();

        request.setEmail(customer.getEmail());

        assertDoesNotThrow(
                () -> service.checkUpdate(customer, request)
        );

        verify(customerRepository, never())
                .existsByEmail(anyString());
    }

    @Test
    void update_shouldEncodePassword_whenPasswordIsRaw() {

        customer.setPassword("123456");

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded");

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(i -> i.getArgument(0));

        Customer result = service.update(customer);

        assertEquals("encoded", result.getPassword());
    }

    @Test
    void update_shouldEncodePasswordAndSave() {

        customer.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword"))
                .thenReturn("encodedPassword");

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(i -> i.getArgument(0));

        Customer result = service.update(customer);

        assertEquals("encodedPassword", result.getPassword());

        verify(customerRepository)
                .save(any(Customer.class));
    }

    @Test
    void update_shouldNotEncodePassword_whenAlreadyEncoded() {

        customer.setPassword("$2a$encodedPassword");

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(i -> i.getArgument(0));

        service.update(customer);

        verify(passwordEncoder, never())
                .encode(anyString());
    }
    @Test
    void verifyEmail_shouldSetEmailVerifiedTrue() {

        customer.setEmailVerified(false);

        service.verifyEmail(customer);

        assertTrue(customer.isEmailVerified());

        verify(customerRepository)
                .save(customer);
    }

    @Test
    void existsByEmail_shouldReturnTrue() {

        when(customerRepository.existsByEmail("test@mail.com"))
                .thenReturn(true);

        boolean result =
                service.existsByEmail("test@mail.com");

        assertTrue(result);
    }

    @Test
    void existsByEmail_shouldReturnFalse() {

        when(customerRepository.existsByEmail("x@mail.com"))
                .thenReturn(false);

        assertFalse(
                service.existsByEmail("x@mail.com")
        );
    }
}