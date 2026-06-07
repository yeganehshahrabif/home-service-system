package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.OfferStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OfferResponse {
    private Long id;
    private Double proposedPrice;
    private LocalDateTime proposedStartTime;
    private Integer durationHours;
    private LocalDateTime offerDate;
    private OfferStatus status;
    private Long expertId;
    private String expertName;
    private Double expertRating;  
}