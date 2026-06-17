package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentResponse {

    private Long orderId;

    private Long customerId;

    private BigDecimal paidAmount;

    private BigDecimal expertShare;

    private BigDecimal systemShare;

    private PaymentStatus paymentStatus;

    private String message;
}