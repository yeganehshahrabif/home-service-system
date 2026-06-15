package ir.maktabsharif138.home_service_system.dto.request;

import ir.maktabsharif138.home_service_system.entity.enums.Role;
import lombok.Data;

@Data
public class UserSearchRequest {

    private Role role;

    private String name;

    private Long homeServiceId;

    private Double minRating;

    private Double maxRating;
}