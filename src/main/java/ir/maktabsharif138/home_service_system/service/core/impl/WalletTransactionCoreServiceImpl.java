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

    // ---------------- DEPOSIT ----------------

    @Override
    @Transactional
    public WalletTransaction deposit(Wallet wallet, BigDecimal amount, String description) {

        checkWalletAndAmount(wallet, amount);

        WalletTransaction tx = create(wallet, TransactionType.DEPOSIT, amount, description);

        tx.setBalanceAfterTransaction(wallet.getBalance());

        return recordTransaction(tx);
    }

    // ---------------- WITHDRAW ----------------

    @Override
    @Transactional
    public WalletTransaction withdraw(Wallet wallet, BigDecimal amount, String description) {

        checkWalletAndAmount(wallet, amount);

        WalletTransaction tx = create(wallet, TransactionType.WITHDRAWAL, amount, description);

        tx.setBalanceAfterTransaction(wallet.getBalance());

        return recordTransaction(tx);
    }

    // ---------------- TRANSFER ----------------

    @Override
    @Transactional
    public void transfer(Wallet source, Wallet destination, BigDecimal amount, String description) {

        checkWalletAndAmount(source, amount);
        checkWalletAndAmount(destination, amount);

        WalletTransaction out = recordTransaction(create(source, TransactionType.WITHDRAWAL,
                amount, "TRANSFER OUT: " + description));
        out.setBalanceAfterTransaction(source.getBalance());
        repository.save(out);

        WalletTransaction in = recordTransaction(create(destination, TransactionType.DEPOSIT, amount,
                "TRANSFER IN: " + description));
        in.setBalanceAfterTransaction(destination.getBalance());
        repository.save(in);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long walletId) {

        return repository.calculateBalance(walletId);
    }

    // ---------------- PAGINATION (FIXED) ----------------

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

    // ---------------- BALANCE (CORRECT WAY) ----------------

    // ---------------- CORE ----------------

    private WalletTransaction create(
            Wallet wallet,
            TransactionType type,
            BigDecimal amount,
            String description
    ) {
        WalletTransaction tx = new WalletTransaction();
        tx.setWallet(wallet);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setCreatedAt(LocalDateTime.now());

        return tx;
    }

    private WalletTransaction recordTransaction(WalletTransaction tx) {

        // ⚠️ مهم: balanceAfterTransaction نباید از wallet خونده بشه
        // چون wallet ممکنه هنوز commit نشده باشه

        tx.setBalanceAfterTransaction(tx.getWallet().getBalance());

        return repository.save(tx);
    }

    private void checkWalletAndAmount(Wallet wallet, BigDecimal amount) {

        if (Objects.isNull(wallet))
            throw new NotFoundException("WALLET_NOT_FOUND");

        if (Objects.isNull(amount) || amount.signum() <= 0)
            throw new BadRequestException("INVALID_AMOUNT");
    }
}