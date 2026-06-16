package ir.maktabsharif138.home_service_system.entity;
import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
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

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WALLET_COLUMN, nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PAYMENT_COLUMN)
    private Payment payment;

    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}