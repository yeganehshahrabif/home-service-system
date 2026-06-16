package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomeServiceUpdateRequest {

    private String name;
    private String description;
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
    private Long parentServiceId;
}