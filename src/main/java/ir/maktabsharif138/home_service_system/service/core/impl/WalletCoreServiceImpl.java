package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.WalletRepository;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletTransactionCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WalletCoreServiceImpl implements WalletCoreService {

    private final WalletRepository walletRepository;
    private final WalletTransactionCoreService txService;

    @Override
    @Transactional(readOnly = true)
    public Wallet findById(Long walletId) {

        return walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new NotFoundException("WALLET_NOT_FOUND"));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long walletId) {

        return findById(walletId).getBalance();
    }

    @Override
    @Transactional
    public void credit(Long walletId, BigDecimal amount, String reason) {

        checkAmount(amount);

        Wallet wallet = findById(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        txService.deposit(wallet, amount, reason);
    }

    @Override
    @Transactional
    public void debit(Long walletId, BigDecimal amount, String reason) {

        checkAmount(amount);
        if (!hasSufficientBalance(walletId, amount)) {
            throw new BadRequestException(
                    "INSUFFICIENT_BALANCE"
            );
        }

        Wallet wallet = findById(walletId);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        txService.withdraw(wallet, amount, reason);
    }

    @Override
    @Transactional
    public void transfer(Long sourceWalletId, Long destWalletId, BigDecimal amount, String reason) {

        checkTransfer(sourceWalletId, destWalletId);
        checkAmount(amount);
        if (!hasSufficientBalance(sourceWalletId, amount)) {
            throw new BadRequestException(
                    "INSUFFICIENT_BALANCE"
            );
        }

        Wallet source = findById(sourceWalletId);
        Wallet destination = findById(destWalletId);
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));
        walletRepository.save(source);
        walletRepository.save(destination);

        txService.transfer(source, destination, amount, reason);
    }

    private boolean hasSufficientBalance(Long walletId, BigDecimal amount) {

        Wallet wallet = findById(walletId);

        return wallet.getBalance().compareTo(amount) >= 0;
    }

    private void checkAmount(BigDecimal amount) {

        if (Objects.isNull(amount) || amount.signum() <= 0) {

            throw new BadRequestException(
                    "INVALID_AMOUNT"
            );
        }
    }

    private void checkTransfer(Long sourceWalletId, Long destWalletId) {

        if (sourceWalletId.equals(destWalletId)) {
            throw new BadRequestException(
                    "INVALID_TRANSFER"
            );
        }
    }
}