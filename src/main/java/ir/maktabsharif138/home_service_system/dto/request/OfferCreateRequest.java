package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OfferCreateRequest {
    @NotNull(message = "Proposed price is required")
    @Positive(message = "Proposed price must be positive")
    private BigDecimal proposedPrice;

    @NotNull(message = "Proposed start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime proposedStartTime;

    @NotNull(message = "Duration hours is required")
    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    @NotNull(message = "Order ID is required")
    private Long orderId;

}