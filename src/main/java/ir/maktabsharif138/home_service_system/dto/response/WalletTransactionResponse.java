package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class WalletTransactionResponse {

    private Long id;

    private TransactionType type;

    private BigDecimal amount;

    private BigDecimal balanceAfterTransaction;

    private String description;

    private LocalDateTime createdAt;

    private Long walletId;

}