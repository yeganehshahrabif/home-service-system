package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.response.BalanceResponse;
import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.mapper.WalletTransactionMapper;
import ir.maktabsharif138.home_service_system.security.CurrentUserService;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletTransactionCoreService;
import ir.maktabsharif138.home_service_system.service.facade.WalletFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletFacadeServiceImpl implements WalletFacadeService {

    private final CurrentUserService currentUserService;

    private final WalletCoreService walletCoreService;
    private final WalletTransactionCoreService transactionCoreService;

    private final WalletTransactionMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalanceForCustomer() {

        Wallet wallet = walletCoreService.findByCustomerId(getCurrentUserId());

        return new BalanceResponse(wallet.getId(), wallet.getBalance());
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalanceForExpert() {

        Wallet wallet = walletCoreService.findByExpertId(getCurrentUserId());

        return new BalanceResponse(wallet.getId(), wallet.getBalance());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransactionResponse> getCustomerTransactions(
            Pageable pageable
    ) {

        Wallet wallet = walletCoreService.findByCustomerId(getCurrentUserId());
        return getTransactions(wallet.getId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransactionResponse> getExpertTransactions(Pageable pageable) {

        Wallet wallet = walletCoreService.findByExpertId(getCurrentUserId());
        return getTransactions(wallet.getId(), pageable);
    }

    private Long getCurrentUserId() {
        return currentUserService.getCurrentUserId();
    }

    private Page<WalletTransactionResponse> getTransactions(Long walletId, Pageable pageable) {

        return transactionCoreService
                .findByWalletId(walletId, pageable)
                .map(mapper::toResponse);
    }
}