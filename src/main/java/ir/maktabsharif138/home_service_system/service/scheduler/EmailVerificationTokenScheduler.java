package ir.maktabsharif138.home_service_system.service.scheduler;

import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationTokenScheduler {

    private final EmailVerificationTokenCoreService
            emailVerificationTokenCoreService;

    @Scheduled(cron = "0 0 2 * * *")
    public void deleteExpiredTokens() {

        emailVerificationTokenCoreService
                .deleteExpiredTokens();
    }
}