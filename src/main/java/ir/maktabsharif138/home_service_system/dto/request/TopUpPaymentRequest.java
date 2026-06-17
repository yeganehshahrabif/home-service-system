package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopUpPaymentRequest {

    @NotNull
    private Long customerId;

    @NotNull
    @DecimalMin("1000")
    private BigDecimal amount;
}