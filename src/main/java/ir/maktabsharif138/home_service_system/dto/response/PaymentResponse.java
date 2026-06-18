package ir.maktabsharif138.home_service_system.dto.response;
import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponse {

    private Long paymentId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private String paymentLink;

    private String message;
}