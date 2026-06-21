package ir.maktabsharif138.home_service_system.service.scheduler;

import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentCoreService paymentCoreService;

    @Scheduled(fixedRate = 60000)
    public void expirePayments() {

        int count = paymentCoreService.expireOldPayments();
        if(count > 0) {
            log.info("{} payments expired", count);
        }
    }
}