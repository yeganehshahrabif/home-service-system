package ir.maktabsharif138.home_service_system.service.integration.captcha;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CaptchaStorage {

    private static class CaptchaData {
        private final String value;
        private final long expireTime;

        public CaptchaData(String value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public String getValue() {
            return value;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }

    private final Map<String, CaptchaData> storage = new ConcurrentHashMap<>();
    private static final long TTL_MILLIS = 2 * 60 * 1000;

    public void save(String key, String value) {
        storage.put(key, new CaptchaData(
                value,
                System.currentTimeMillis() + TTL_MILLIS
        ));
    }

    public String get(String key) {

        CaptchaData data = storage.get(key);

        if (data == null) {
            return null;
        }

        if (System.currentTimeMillis() > data.getExpireTime()) {
            storage.remove(key);
            return null;
        }

        return data.getValue();
    }

    public void remove(String key) {
        storage.remove(key);
    }

    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        storage.entrySet().removeIf(e -> e.getValue().getExpireTime() < now);
    }
}