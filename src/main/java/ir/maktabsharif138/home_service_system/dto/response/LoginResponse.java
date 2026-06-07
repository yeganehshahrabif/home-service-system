package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String email;
    private String role;
}