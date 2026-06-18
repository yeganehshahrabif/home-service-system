package ir.maktabsharif138.home_service_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceResponse {

    private Long walletId;

    private BigDecimal balance;
}