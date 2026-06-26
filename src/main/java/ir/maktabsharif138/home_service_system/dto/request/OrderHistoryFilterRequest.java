package ir.maktabsharif138.home_service_system.dto.request;

import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderHistoryFilterRequest {

    private OrderStatus status;
}