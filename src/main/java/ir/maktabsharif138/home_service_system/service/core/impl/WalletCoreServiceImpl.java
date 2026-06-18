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
    public Wallet findByCustomerId(Long customerId) {
        return walletRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new NotFoundException("WALLET_NOT_FOUND"));
    }

    @Override
    public Wallet findByExpertId(Long expertId) {
        return walletRepository.findByExpertId(expertId)
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
        Wallet wallet = findById(walletId);
        checkAmount(amount);
        checkReason(reason);

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        txService.deposit(wallet, amount, reason);
    }

    @Override
    @Transactional
    public void debit(Long walletId, BigDecimal amount, String reason) {
        Wallet wallet = findById(walletId);
        checkAmount(amount);
        checkReason(reason);

        ensureSufficientBalance(wallet, amount);

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        txService.withdraw(wallet, amount, reason);
    }

    private void ensureSufficientBalance(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient balance");
        }
    }

    private void checkAmount(BigDecimal amount) {

        if (Objects.isNull(amount) || amount.signum() <= 0) {

            throw new BadRequestException(
                    "invalid amount"
            );
        }
    }
    private void checkReason(String reason) {
        if (Objects.isNull(reason) || reason.isBlank()) {
            throw new BadRequestException("invalid reason");
        }
    }

}