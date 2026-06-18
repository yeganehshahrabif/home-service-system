package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.entity.WalletTransaction;
import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.WalletTransactionRepository;
import ir.maktabsharif138.home_service_system.service.core.WalletTransactionCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WalletTransactionCoreServiceImpl implements WalletTransactionCoreService {

    private final WalletTransactionRepository repository;


    @Override
    @Transactional
    public WalletTransaction deposit(Wallet wallet, BigDecimal amount, String description) {

        WalletTransaction tx =
                WalletTransaction.deposit(
                        wallet,
                        amount,
                        description,
                        wallet.getBalance()
                );

        return repository.save(tx);
    }


    @Override
    @Transactional
    public WalletTransaction withdraw(Wallet wallet, BigDecimal amount, String description) {

        WalletTransaction tx =
                WalletTransaction.withdraw(
                        wallet,
                        amount,
                        description,
                        wallet.getBalance()
                );


        return repository.save(tx);
    }


    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long walletId) {

        return repository.calculateBalance(walletId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransaction> findByWalletId(Long walletId, Pageable pageable) {
        return repository.findByWalletId(walletId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransaction> findByWalletIdAndType(Long walletId, TransactionType type, Pageable pageable) {

        return repository.findByWalletIdAndType(walletId, type, pageable);
    }


}