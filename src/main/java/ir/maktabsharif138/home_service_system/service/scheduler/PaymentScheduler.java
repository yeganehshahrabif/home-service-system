package ir.maktabsharif138.home_service_system.service.scheduler;

import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentCoreService paymentCoreService;

    @Scheduled(fixedRate = 60000)
    public void expirePayments() {

        paymentCoreService.expireOldPayments();
    }
}