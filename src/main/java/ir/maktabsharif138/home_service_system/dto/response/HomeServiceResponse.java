package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HomeServiceResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Long parentServiceId;
    private List<HomeServiceResponse> subServices;
}