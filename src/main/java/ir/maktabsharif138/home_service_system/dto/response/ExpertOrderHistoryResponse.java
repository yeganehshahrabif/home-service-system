package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpertOrderHistoryResponse {

    private Long orderId;
    private String description;
    private Double proposedPrice;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String customerName;
    private String homeServiceName;
    private Long acceptedOfferId;
}