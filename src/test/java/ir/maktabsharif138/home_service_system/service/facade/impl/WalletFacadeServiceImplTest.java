package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.response.BalanceResponse;
import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.entity.WalletTransaction;
import ir.maktabsharif138.home_service_system.mapper.WalletTransactionMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletTransactionCoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletFacadeServiceImplTest {

    @Mock
    private CustomerCoreService customerCoreService;

    @Mock
    private ExpertCoreService expertCoreService;

    @Mock
    private WalletCoreService walletCoreService;

    @Mock
    private WalletTransactionCoreService transactionCoreService;

    @Mock
    private WalletTransactionMapper mapper;

    @InjectMocks
    private WalletFacadeServiceImpl facade;

    @Test
    void getBalanceForCustomer_shouldReturnBalanceResponse() {

        Wallet wallet = new Wallet();
        wallet.setId(10L);

        Customer customer = new Customer();
        customer.setWallet(wallet);

        when(customerCoreService.findById(1L))
                .thenReturn(customer);

        when(walletCoreService.getBalance(10L))
                .thenReturn(BigDecimal.valueOf(500));

        BalanceResponse result =
                facade.getBalanceForCustomer(1L);

        assertNotNull(result);
        assertEquals(10L, result.getWalletId());
        assertEquals(BigDecimal.valueOf(500), result.getBalance());

        verify(customerCoreService).findById(1L);
        verify(walletCoreService).getBalance(10L);
    }

    @Test
    void getBalanceForExpert_shouldReturnBalanceResponse() {

        Wallet wallet = new Wallet();
        wallet.setId(20L);

        Expert expert = new Expert();
        expert.setWallet(wallet);

        when(expertCoreService.findById(2L))
                .thenReturn(expert);

        when(walletCoreService.getBalance(20L))
                .thenReturn(BigDecimal.valueOf(800));

        BalanceResponse result =
                facade.getBalanceForExpert(2L);

        assertNotNull(result);
        assertEquals(20L, result.getWalletId());
        assertEquals(BigDecimal.valueOf(800), result.getBalance());

        verify(expertCoreService).findById(2L);
        verify(walletCoreService).getBalance(20L);
    }

    @Test
    void getCustomerTransactions_shouldReturnMappedPage() {

        Wallet wallet = new Wallet();
        wallet.setId(30L);

        Customer customer = new Customer();
        customer.setWallet(wallet);

        WalletTransaction tx = new WalletTransaction();

        WalletTransactionResponse response =
                mock(WalletTransactionResponse.class);

        Page<WalletTransaction> page =
                new PageImpl<>(List.of(tx));

        when(customerCoreService.findById(1L))
                .thenReturn(customer);

        when(transactionCoreService.findByWalletId(30L, Pageable.unpaged()))
                .thenReturn(page);

        when(mapper.toResponse(tx))
                .thenReturn(response);

        Page<WalletTransactionResponse> result =facade.getCustomerTransactions(1L, Pageable.unpaged());

        assertEquals(1, result.getContent().size());

        verify(transactionCoreService)
                .findByWalletId(30L, Pageable.unpaged());
    }

    @Test
    void getExpertTransactions_shouldReturnMappedPage() {

        Wallet wallet = new Wallet();
        wallet.setId(40L);

        Expert expert = new Expert();
        expert.setWallet(wallet);

        WalletTransaction tx = new WalletTransaction();

        WalletTransactionResponse response =
                mock(WalletTransactionResponse.class);

        Page<WalletTransaction> page =
                new PageImpl<>(List.of(tx));

        when(expertCoreService.findById(2L))
                .thenReturn(expert);

        when(transactionCoreService.findByWalletId(40L, Pageable.unpaged()))
                .thenReturn(page);

        when(mapper.toResponse(tx))
                .thenReturn(response);

        Page<WalletTransactionResponse> result =
                facade.getExpertTransactions(2L, Pageable.unpaged());

        assertEquals(1, result.getContent().size());

        verify(transactionCoreService)
                .findByWalletId(40L, Pageable.unpaged());
    }
}