package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import ir.maktabsharif138.home_service_system.mapper.WalletTransactionMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletTransactionCoreService;
import ir.maktabsharif138.home_service_system.service.facade.WalletFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletFacadeServiceImpl implements WalletFacadeService {

    private final CustomerCoreService customerCoreService;
    private final ExpertCoreService expertCoreService;

    private final WalletCoreService walletCoreService;
    private final WalletTransactionCoreService transactionCoreService;

    private final WalletTransactionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceForCustomer(Long customerId) {

        return walletCoreService.getBalance(getCustomerWalletId(customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceForExpert(Long expertId) {

        return walletCoreService.getBalance(getExpertWalletId(expertId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransactionResponse> getCustomerTransactions(
            Long customerId,
            Pageable pageable
    ) {

        return getTransactions(getCustomerWalletId(customerId), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransactionResponse> getExpertTransactions(Long expertId, Pageable pageable) {

        return getTransactions(getExpertWalletId(expertId), pageable);
    }

    private Long getCustomerWalletId(Long customerId) {

        return customerCoreService.findById(customerId).getWallet().getId();
    }

    private Long getExpertWalletId(Long expertId) {

        return expertCoreService.findById(expertId).getWallet().getId();
    }

    private Page<WalletTransactionResponse> getTransactions(Long walletId, Pageable pageable) {

        return transactionCoreService
                .findByWalletId(walletId, pageable)
                .map(mapper::toResponse);
    }
}