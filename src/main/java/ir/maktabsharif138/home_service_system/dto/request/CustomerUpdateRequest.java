package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CustomerUpdateRequest {

    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    private String password;
}