package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    @NotNull
    private Long walletId;

    @NotNull
    private BigDecimal amount;

    private String description;
}