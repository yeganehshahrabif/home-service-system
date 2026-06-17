package ir.maktabsharif138.home_service_system.service.integration.captcha;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CaptchaGenerator {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();

    // تولید متن کپچا
    public String generateText() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return sb.toString();
    }


    public String generateImage(String text) {

        // در نسخه واقعی: BufferedImage + Graphics2D + Base64
        return "data:image/png;base64,FAKE_CAPTCHA_IMAGE_FOR_" + text;
    }
}