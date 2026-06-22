package ir.maktabsharif138.home_service_system.service.scheduler;

import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaptchaCleanUpSchedularTest {

    @Mock
    private CaptchaStorage captchaStorage;

    @InjectMocks
    private CaptchaCleanUpSchedular schedular;

    @Test
    void clean_shouldCallCleanupExpired() {

        schedular.clean();

        verify(captchaStorage, times(1))
                .cleanupExpired();
    }
}