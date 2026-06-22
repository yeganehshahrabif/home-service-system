package ir.maktabsharif138.home_service_system.service.integration.captcha;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CaptchaGeneratorTest {

    @InjectMocks
    private CaptchaGenerator captchaGenerator;

    @Test
    void generateText_shouldReturnFiveCharacters() {

        String result = captchaGenerator.generateText();

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[A-Z2-9]{5}"));
    }

    @Test
    void generateText_shouldContainOnlyValidCharacters() {

        String result = captchaGenerator.generateText();

        String validChars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

        for (char c : result.toCharArray()) {
            assertTrue(validChars.indexOf(c) >= 0);
        }
    }

    @Test
    void generateImage_shouldReturnBase64Image() {

        String text = "ABCDE";

        String result = captchaGenerator.generateImage(text);

        assertNotNull(result);
        assertTrue(result.startsWith("data:image/png;base64,"));
    }

    @Test
    void generateImage_shouldReturnNonEmptyBase64Content() {

        String text = "ABCDE";

        String result = captchaGenerator.generateImage(text);

        String base64 = result.replace("data:image/png;base64,", "");

        assertFalse(base64.isBlank());
        assertTrue(base64.length() > 50);
    }
}