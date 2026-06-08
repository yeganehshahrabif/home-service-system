package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerUpdateRequest {

    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain letters and numbers")
    private String password;
}