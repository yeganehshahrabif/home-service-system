package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpertOrderRatingResponse {

    private Long orderId;
    private Integer rating;
    private LocalDateTime reviewDate;
}