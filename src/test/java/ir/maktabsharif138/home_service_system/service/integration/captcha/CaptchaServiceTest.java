package ir.maktabsharif138.home_service_system.service.integration.captcha;

import ir.maktabsharif138.home_service_system.dto.response.CaptchaResponse;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaptchaServiceTest {

    @Mock
    private CaptchaGenerator generator;

    @Mock
    private CaptchaStorage storage;

    @InjectMocks
    private CaptchaService captchaService;

    private final String key = "test-key";
    private final String text = "abcd";
    private final String image = "image-bytes";

    @Test
    void generateCaptcha_shouldReturnResponse() {

        when(generator.generateText()).thenReturn(text);
        when(generator.generateImage(text)).thenReturn(image);

        CaptchaResponse response = captchaService.generateCaptcha();

        assertNotNull(response);
        assertNotNull(response.getKey());
        assertEquals(image, response.getImage());

        verify(generator).generateText();
        verify(generator).generateImage(text);
        verify(storage).save(anyString(), eq(text));
    }

    @Test
    void validate_shouldPass_whenInputIsCorrect() {

        when(storage.get(key)).thenReturn(text);

        captchaService.validate(key, text);

        verify(storage).get(key);
        verify(storage).remove(key);
    }

    @Test
    void validate_shouldThrowException_whenKeyIsEmpty() {

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> captchaService.validate("", text)
        );

        assertEquals("CAPTCHA_REQUIRED", ex.getMessage());
    }

    @Test
    void validate_shouldThrowException_whenInputIsEmpty() {

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> captchaService.validate(key, "")
        );

        assertEquals("CAPTCHA_REQUIRED", ex.getMessage());
    }

    @Test
    void validate_shouldThrowException_whenCaptchaNotFound() {

        when(storage.get(key)).thenReturn(null);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> captchaService.validate(key, text)
        );

        assertEquals("CAPTCHA_EXPIRED_OR_NOT_FOUND", ex.getMessage());
    }

    @Test
    void validate_shouldThrowException_whenCaptchaIsWrong() {

        when(storage.get(key)).thenReturn("real-value");

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> captchaService.validate(key, "wrong")
        );

        assertEquals("INVALID_CAPTCHA", ex.getMessage());
    }
}