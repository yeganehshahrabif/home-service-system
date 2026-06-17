package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull
    private Long sourceWalletId;

    @NotNull
    private Long destinationWalletId;

    @NotNull
    private BigDecimal amount;

    private String reason;
}