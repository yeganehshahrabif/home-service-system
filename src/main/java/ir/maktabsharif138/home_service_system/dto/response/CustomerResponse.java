package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
}