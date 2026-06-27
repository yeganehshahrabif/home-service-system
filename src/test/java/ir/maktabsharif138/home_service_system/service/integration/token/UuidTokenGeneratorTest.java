package ir.maktabsharif138.home_service_system.service.integration.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UuidTokenGeneratorTest {

    private final UuidTokenGenerator generator =
            new UuidTokenGenerator();

    @Test
    void generate_shouldReturnValue() {

        String token = generator.generate();

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generate_shouldBeUnique() {

        String t1 = generator.generate();
        String t2 = generator.generate();

        assertNotEquals(t1, t2);
    }
}