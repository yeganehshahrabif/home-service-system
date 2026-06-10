package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderCreateRequest {
    private String description;

    @NotNull(message = "Proposed price is required")
    @Positive(message = "Proposed price must be positive")
    private Double proposedPrice;

    @NotNull(message = "Start date and time is required")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDateTime;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Service ID is required")
    private Long homeServiceId;
}