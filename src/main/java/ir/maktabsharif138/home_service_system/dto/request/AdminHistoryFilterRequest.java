package ir.maktabsharif138.home_service_system.dto.request;

import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminHistoryFilterRequest {

    private OrderStatus status;

    private Long homeServiceId;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;
}