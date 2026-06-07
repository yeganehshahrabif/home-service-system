package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ExpertUpdateRequest {
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private String profileImage;
}