package ir.maktabsharif138.home_service_system.service.scheduler;

import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaptchaCleanUpSchedular {

    private final CaptchaStorage captchaStorage;

    @Scheduled(fixedRate = 60000)
    public void clean() {
        captchaStorage.cleanupExpired();
    }
}