package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.entity.WalletTransaction;
import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
import ir.maktabsharif138.home_service_system.repository.WalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletTransactionCoreServiceImplTest {

    @Mock
    private WalletTransactionRepository repository;

    @InjectMocks
    private WalletTransactionCoreServiceImpl service;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void deposit_shouldSaveTransaction() {

        when(repository.save(any(WalletTransaction.class)))
                .thenAnswer(i -> i.getArgument(0));

        WalletTransaction result =
                service.deposit(wallet, BigDecimal.valueOf(200), "DEPOSIT");

        assertNotNull(result);

        verify(repository).save(any(WalletTransaction.class));
    }

    @Test
    void withdraw_shouldSaveTransaction() {

        when(repository.save(any(WalletTransaction.class)))
                .thenAnswer(i -> i.getArgument(0));

        WalletTransaction result =
                service.withdraw(wallet, BigDecimal.valueOf(100), "WITHDRAW");

        assertNotNull(result);

        verify(repository).save(any(WalletTransaction.class));
    }

    @Test
    void calculateBalance_shouldReturnValue() {

        when(repository.calculateBalance(1L))
                .thenReturn(BigDecimal.valueOf(500));

        BigDecimal result = service.calculateBalance(1L);

        assertEquals(BigDecimal.valueOf(500), result);

        verify(repository).calculateBalance(1L);
    }

    @Test
    void findByWalletId_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByWalletId(1L, pageable))
                .thenReturn(Page.empty());

        Page<WalletTransaction> result =
                service.findByWalletId(1L, pageable);

        assertNotNull(result);

        verify(repository).findByWalletId(1L, pageable);
    }

    @Test
    void findByWalletIdAndType_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByWalletIdAndType(1L, TransactionType.DEPOSIT, pageable))
                .thenReturn(Page.empty());

        Page<WalletTransaction> result =
                service.findByWalletIdAndType(1L, TransactionType.DEPOSIT, pageable);

        assertNotNull(result);

        verify(repository).findByWalletIdAndType(1L, TransactionType.DEPOSIT, pageable);
    }
}