package ir.maktabsharif138.home_service_system.service.integration.captcha;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CaptchaStorage {

    private final Map<String, String> storage = new ConcurrentHashMap<>();

    public void save(String key, String value) {
        storage.put(key, value);
    }

    public String get(String key) {
        return storage.get(key);
    }

    public void remove(String key) {
        storage.remove(key);
    }
}