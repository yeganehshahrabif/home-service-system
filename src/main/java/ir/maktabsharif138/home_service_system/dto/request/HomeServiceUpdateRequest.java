package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HomeServiceUpdateRequest {

    private String name;
    private String description;
    @Positive(message = "Base price must be positive")
    private Double basePrice;
    private Long parentServiceId;
}