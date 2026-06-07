package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class HomeServiceResponse {
    private Long id;
    private String name;
    private String description;
    private Double basePrice;
    private Long parentServiceId;
    private List<HomeServiceResponse> subServices;
}