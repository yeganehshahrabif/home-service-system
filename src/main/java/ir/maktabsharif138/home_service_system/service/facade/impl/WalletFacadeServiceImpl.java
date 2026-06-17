package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Expert;
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
@Transactional(readOnly = true)
public class WalletFacadeServiceImpl
        implements WalletFacadeService {

    private final CustomerCoreService customerCoreService;
    private final ExpertCoreService expertCoreService;
    private final WalletCoreService walletCoreService;
    private final WalletTransactionCoreService transactionCoreService;

    private final WalletTransactionMapper mapper;

    @Override
    public BigDecimal getBalanceForCustomer(Long customerId) {

        Customer customer = customerCoreService.findById(customerId);

        return walletCoreService.getBalance(customer.getWallet().getId());
    }

    @Override
    public BigDecimal getBalanceForExpert(Long expertId) {

        Expert expert = expertCoreService.findById(expertId);

        return walletCoreService.getBalance(expert.getWallet().getId());
    }

    @Override
    public Page<WalletTransactionResponse> getCustomerTransactions(Long customerId, Pageable pageable) {

        Customer customer = customerCoreService.findById(customerId);

        return transactionCoreService
                .findByWalletId(customer.getWallet().getId(), pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<WalletTransactionResponse> getExpertTransactions(Long expertId, Pageable pageable) {

        Expert expert = expertCoreService.findById(expertId);

        return transactionCoreService
                .findByWalletId(expert.getWallet().getId(), pageable)
                .map(mapper::toResponse);
    }
}