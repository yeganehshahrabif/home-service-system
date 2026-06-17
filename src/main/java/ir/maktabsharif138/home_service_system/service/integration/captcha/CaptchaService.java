package ir.maktabsharif138.home_service_system.service.integration.captcha;

import ir.maktabsharif138.home_service_system.dto.response.CaptchaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaGenerator generator;
    private final CaptchaStorage storage;


    public CaptchaResponse generateCaptcha() {

        String text = generator.generateText();
        String key = UUID.randomUUID().toString();

        // ذخیره متن کپچا با کلید
        storage.save(key, text);

        // تولید تصویر (fake یا واقعی)
        String image = generator.generateImage(text);

        return new CaptchaResponse(key, image);
    }


    public void validate(String key, String userInput) {

        String real = storage.get(key);

        if (real == null || !real.equalsIgnoreCase(userInput)) {
            throw new RuntimeException("INVALID_CAPTCHA");
        }

        storage.remove(key);
    }
}