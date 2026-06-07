package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HomeServiceCreateRequest {
    @NotBlank(message = "Service name is required")
    private String name;

    private String description;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private Double basePrice;

    private Long parentServiceId;
}