package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Wallet;

import java.math.BigDecimal;

public interface WalletCoreService {

    Wallet findById(Long walletId);
//    Wallet findByCustomerId(Long customerId);
//    Wallet findByExpertId(Long expertId);
    BigDecimal getBalance(Long walletId);
    void credit(Long walletId, BigDecimal amount, String reason);
    void debit(Long walletId, BigDecimal amount, String reason);
    void transfer(Long sourceWalletId, Long destWalletId, BigDecimal amount, String reason);
}