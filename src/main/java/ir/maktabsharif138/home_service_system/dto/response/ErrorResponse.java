package ir.maktabsharif138.home_service_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private int status;

    private LocalDateTime timestamp;
}