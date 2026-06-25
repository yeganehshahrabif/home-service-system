package ir.maktabsharif138.home_service_system.service.integration.token;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidTokenGenerator
        implements TokenGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}