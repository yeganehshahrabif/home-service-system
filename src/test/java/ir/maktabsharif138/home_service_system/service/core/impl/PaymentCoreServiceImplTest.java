package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.PaymentRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCoreServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerCoreService customerCoreService;

    @Mock
    private WalletCoreService walletCoreService;

    @InjectMocks
    private PaymentCoreServiceImpl service;

    private Customer customer;
    private Payment payment;

    @BeforeEach
    void setUp() {

        Wallet wallet = new Wallet();
        wallet.setId(1L);

        customer = new Customer();
        customer.setId(10L);
        customer.setWallet(wallet);

        payment = new Payment();
        payment.setId(100L);
        payment.setCustomer(customer);
        payment.setAmount(BigDecimal.valueOf(200));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        payment.setPaymentReference("ref-123");
    }

    @Test
    void createTopUpPayment_shouldCreateSuccessfully() {

        when(customerCoreService.findById(10L)).thenReturn(customer);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(i -> i.getArgument(0));

        Payment result = service.createTopUpPayment(10L, BigDecimal.valueOf(200));

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(customer, result.getCustomer());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void createTopUpPayment_shouldThrow_whenAmountInvalid() {

        assertThrows(BadRequestException.class,
                () -> service.createTopUpPayment(10L, BigDecimal.ZERO));
    }

    @Test
    void createTopUpPayment_shouldCallCustomerService() {

        when(customerCoreService.findById(10L)).thenReturn(customer);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(i -> i.getArgument(0));

        service.createTopUpPayment(10L, BigDecimal.valueOf(200));

        verify(customerCoreService).findById(10L);
    }

    @Test
    void verifyPayment_shouldSuccessAndCreditWallet() {

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(i -> i.getArgument(0));

        Payment result = service.verifyPayment(100L);

        assertEquals(PaymentStatus.SUCCESS, result.getStatus());

        verify(walletCoreService).credit(
                eq(1L),
                eq(payment.getAmount()),
                eq("TOP_UP_PAYMENT")
        );
    }

    @Test
    void verifyPayment_shouldThrow_whenNotFound() {

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.verifyPayment(100L));
    }

    @Test
    void verifyPayment_shouldThrow_whenAlreadyVerified() {

        payment.setStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.of(payment));

        assertThrows(BadRequestException.class,
                () -> service.verifyPayment(100L));
    }

    @Test
    void verifyPayment_shouldThrow_whenExpired() {

        payment.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.of(payment));

        assertThrows(BadRequestException.class,
                () -> service.verifyPayment(100L));
    }

    @Test
    void verifyPayment_shouldThrow_whenStatusIsNotPending() {

        payment.setStatus(PaymentStatus.FAILED);

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.of(payment));

        assertThrows(BadRequestException.class,
                () -> service.verifyPayment(100L));
    }

    @Test
    void findById_shouldReturnPayment() {

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.of(payment));

        Payment result = service.findById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    void findById_shouldThrow_whenNotFound() {

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findById(100L));
    }

    @Test
    void findByReference_shouldReturnPayment() {

        when(paymentRepository.findByPaymentReference("ref-123"))
                .thenReturn(Optional.of(payment));

        Payment result = service.findByReference("ref-123");

        assertNotNull(result);
        assertEquals("ref-123", result.getPaymentReference());
    }

    @Test
    void findByReference_shouldThrow_whenNotFound() {

        when(paymentRepository.findByPaymentReference("ref-x"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findByReference("ref-x"));
    }

    @Test
    void expireOldPayments_shouldReturnCount() {

        when(paymentRepository.expirePayments(any(LocalDateTime.class)))
                .thenReturn(5);

        int result = service.expireOldPayments();

        assertEquals(5, result);

        verify(paymentRepository).expirePayments(any(LocalDateTime.class));
    }
}