package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.OrderPaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderPaymentResponse {

    private Long orderId;

    private BigDecimal amount;

    private BigDecimal expertShare;

    private BigDecimal platformShare;

    private String message;

    private OrderPaymentStatus status;
}