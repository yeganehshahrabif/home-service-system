package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExpertResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImagePath;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private Double rating;
    private Integer reviewCount;
}