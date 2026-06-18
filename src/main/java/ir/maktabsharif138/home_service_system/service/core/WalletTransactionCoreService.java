package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.entity.WalletTransaction;
import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface WalletTransactionCoreService {

    WalletTransaction deposit(
            Wallet wallet,
            BigDecimal amount,
            String description
    );

    WalletTransaction withdraw(
            Wallet wallet,
            BigDecimal amount,
            String description
    );

    BigDecimal calculateBalance(Long walletId);

    Page<WalletTransaction> findByWalletId(Long walletId, Pageable pageable);

    Page<WalletTransaction> findByWalletIdAndType(Long walletId, TransactionType type, Pageable pageable);

}