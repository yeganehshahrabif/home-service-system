package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private Double rating;
}