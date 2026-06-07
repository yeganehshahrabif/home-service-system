package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
    private Long customerId;
    private String customerName;
    private Long orderId;
}