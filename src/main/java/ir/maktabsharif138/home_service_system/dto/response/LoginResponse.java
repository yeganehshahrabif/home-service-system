package ir.maktabsharif138.home_service_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;

}

//    private Long id;
//    private String role;