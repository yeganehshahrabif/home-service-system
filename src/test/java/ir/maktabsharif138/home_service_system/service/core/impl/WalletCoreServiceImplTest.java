package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.WalletRepository;
import ir.maktabsharif138.home_service_system.service.core.WalletTransactionCoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletCoreServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionCoreService txService;

    @InjectMocks
    private WalletCoreServiceImpl service;

    private Wallet wallet;

    @BeforeEach
    void setUp() {

        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void findById_shouldReturnWallet_whenExists() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        Wallet result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(walletRepository).findById(1L);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findById(1L));
    }

    @Test
    void findByCustomerId_shouldReturnWallet() {

        when(walletRepository.findByCustomerId(10L))
                .thenReturn(Optional.of(wallet));

        Wallet result = service.findByCustomerId(10L);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());
    }

    @Test
    void findByCustomerId_shouldThrow_whenNotFound() {

        when(walletRepository.findByCustomerId(10L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findByCustomerId(10L));
    }

    @Test
    void findByExpertId_shouldReturnWallet() {

        when(walletRepository.findByExpertId(20L))
                .thenReturn(Optional.of(wallet));

        Wallet result = service.findByExpertId(20L);

        assertNotNull(result);
    }

    @Test
    void findByExpertId_shouldThrow_whenNotFound() {

        when(walletRepository.findByExpertId(20L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findByExpertId(20L));
    }

    @Test
    void getBalance_shouldReturnBalance() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        BigDecimal balance = service.getBalance(1L);

        assertEquals(BigDecimal.valueOf(1000), balance);
    }

    @Test
    void credit_shouldIncreaseBalance_andSave_andCreateTx() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(i -> i.getArgument(0));

        service.credit(1L, BigDecimal.valueOf(500), "TOP_UP");

        assertEquals(BigDecimal.valueOf(1500), wallet.getBalance());

        verify(walletRepository).save(wallet);
        verify(txService).deposit(wallet, BigDecimal.valueOf(500), "TOP_UP");
    }

    @Test
    void credit_shouldThrow_whenAmountInvalid() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.credit(1L, BigDecimal.ZERO, "TOP_UP"));
    }

    @Test
    void credit_shouldThrow_whenReasonInvalid() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.credit(1L, BigDecimal.valueOf(100), ""));
    }

    @Test
    void credit_shouldThrow_whenAmountIsNull() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.credit(1L, null, "TOP_UP"));
    }

    @Test
    void credit_shouldThrow_whenReasonIsNull() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.credit(1L, BigDecimal.valueOf(100), null));
    }

    @Test
    void debit_shouldDecreaseBalance_andSave_andCreateTx() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(i -> i.getArgument(0));

        service.debit(1L, BigDecimal.valueOf(300), "WITHDRAW");

        assertEquals(BigDecimal.valueOf(700), wallet.getBalance());

        verify(walletRepository).save(wallet);
        verify(txService).withdraw(wallet, BigDecimal.valueOf(300), "WITHDRAW");
    }

    @Test
    void debit_shouldThrow_whenInsufficientBalance() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.debit(1L, BigDecimal.valueOf(2000), "WITHDRAW"));
    }

    @Test
    void debit_shouldThrow_whenAmountInvalid() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.debit(1L, BigDecimal.ZERO, "WITHDRAW"));
    }

    @Test
    void debit_shouldThrow_whenReasonInvalid() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.debit(1L, BigDecimal.valueOf(100), " "));
    }

    @Test
    void debit_shouldThrow_whenAmountIsNull() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.debit(1L, null, "WITHDRAW"));
    }

    @Test
    void debit_shouldThrow_whenReasonIsNull() {

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class,
                () -> service.debit(1L, BigDecimal.valueOf(100), null));
    }
}