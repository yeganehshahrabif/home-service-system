package ir.maktabsharif138.home_service_system.entity;
import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction extends BaseEntity<Long> {

    private static final String WALLET_COLUMN = "wallet_id";
    private static final String PAYMENT_COLUMN = "payment_id";
    private static final String CUSTOMER_ORDER_COLUMN = "customer_order_id";

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceAfterTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WALLET_COLUMN, nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PAYMENT_COLUMN)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CUSTOMER_ORDER_COLUMN)
    private CustomerOrder customerOrder;

    public static WalletTransaction deposit(
            Wallet wallet,
            BigDecimal amount,
            String description,
            BigDecimal balanceAfter
    ) {
        validate(wallet, amount);

        WalletTransaction tx = new WalletTransaction();

        tx.wallet = wallet;
        tx.amount = amount;
        tx.type = TransactionType.DEPOSIT;
        tx.description = description;
        tx.balanceAfterTransaction = balanceAfter;
        tx.createdAt = LocalDateTime.now();

        return tx;
    }
    public static WalletTransaction withdraw(
            Wallet wallet,
            BigDecimal amount,
            String description,
            BigDecimal balanceAfter
    ) {
        validate(wallet, amount);

        WalletTransaction tx = new WalletTransaction();

        tx.wallet = wallet;
        tx.amount = amount;
        tx.type = TransactionType.WITHDRAWAL;
        tx.description = description;
        tx.balanceAfterTransaction = balanceAfter;
        tx.createdAt = LocalDateTime.now();

        return tx;
    }

    private static void validate(Wallet wallet, BigDecimal amount) {

        if (wallet == null) {
            throw new NotFoundException("WALLET_NOT_FOUND");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new BadRequestException("INVALID_AMOUNT");
        }
    }
}