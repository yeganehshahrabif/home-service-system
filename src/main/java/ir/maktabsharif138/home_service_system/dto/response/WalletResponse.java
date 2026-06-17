package ir.maktabsharif138.home_service_system.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponse {

    private Long id;

    private BigDecimal balance;

    private Long customerId;

    private Long expertId;
}