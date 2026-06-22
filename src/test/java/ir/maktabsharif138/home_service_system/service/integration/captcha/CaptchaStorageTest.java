package ir.maktabsharif138.home_service_system.service.integration.captcha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CaptchaStorageTest {

    private CaptchaStorage storage;

    @BeforeEach
    void setUp() {
        storage = new CaptchaStorage();
    }

    @Test
    void saveAndGet_shouldReturnStoredValue() {

        storage.save("key1", "ABCDE");

        String result = storage.get("key1");

        assertEquals("ABCDE", result);
    }

    @Test
    void get_shouldReturnNull_whenKeyDoesNotExist() {

        String result = storage.get("unknown");

        assertNull(result);
    }

    @Test
    void remove_shouldDeleteCaptcha() {

        storage.save("key1", "ABCDE");

        storage.remove("key1");

        assertNull(storage.get("key1"));
    }

    @Test
    void cleanupExpired_shouldRemoveExpiredCaptchas() throws Exception {

        storage.save("key1", "ABCDE");

        Field storageField =
                CaptchaStorage.class.getDeclaredField("storage");

        storageField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Object> internalStorage =
                (Map<String, Object>) storageField.get(storage);

        Object captchaData = internalStorage.get("key1");

        Field expireField =
                captchaData.getClass().getDeclaredField("expireTime");

        expireField.setAccessible(true);

        expireField.setLong(
                captchaData,
                System.currentTimeMillis() - 1000
        );

        storage.cleanupExpired();

        assertNull(storage.get("key1"));
    }

    @Test
    void get_shouldReturnNull_whenCaptchaIsExpired() throws Exception {

        storage.save("key1", "ABCDE");

        Field storageField =
                CaptchaStorage.class.getDeclaredField("storage");

        storageField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Object> internalStorage =
                (Map<String, Object>) storageField.get(storage);

        Object captchaData = internalStorage.get("key1");

        Field expireField =
                captchaData.getClass().getDeclaredField("expireTime");

        expireField.setAccessible(true);

        expireField.setLong(
                captchaData,
                System.currentTimeMillis() - 1000
        );

        String result = storage.get("key1");

        assertNull(result);
    }
}