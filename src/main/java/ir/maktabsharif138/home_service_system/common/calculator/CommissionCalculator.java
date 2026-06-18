package ir.maktabsharif138.home_service_system.common.calculator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CommissionCalculator {

    private static final BigDecimal EXPERT_PERCENT = BigDecimal.valueOf(70);

    public BigDecimal expertShare(BigDecimal amount) {
        return amount
                .multiply(EXPERT_PERCENT)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal platformShare(BigDecimal amount) {
        return amount.subtract(expertShare(amount));
    }
}