package ir.maktabsharif138.home_service_system.service.integration.captcha;

import ir.maktabsharif138.home_service_system.dto.response.CaptchaResponse;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaGenerator;
import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaGenerator generator;
    private final CaptchaStorage storage;


    public CaptchaResponse generateCaptcha() {

        String text = generator.generateText();
        String key = UUID.randomUUID().toString();

        storage.save(key, text);

        return new CaptchaResponse(
                key,
                generator.generateImage(text)
        );
    }

    public void validate(String key, String userInput) {

        if (!StringUtils.hasText(key) || !StringUtils.hasText(userInput)) {
            throw new BadRequestException("CAPTCHA_REQUIRED");
        }

        String realValue = storage.get(key);

        if (realValue == null) {
            throw new BadRequestException("CAPTCHA_EXPIRED_OR_NOT_FOUND");
        }

        if (!realValue.equalsIgnoreCase(userInput.trim())) {
            throw new BadRequestException("INVALID_CAPTCHA");
        }

        storage.remove(key); // one-time use
    }
}