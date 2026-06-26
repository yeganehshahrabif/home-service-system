package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderHistorySummaryResponse {

    private Long orderId;

    private String homeServiceName;

    private OrderStatus orderStatus;

    private BigDecimal finalPrice;

    private LocalDateTime orderDate;
}