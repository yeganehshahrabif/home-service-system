package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmRechargeRequest {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;
}
