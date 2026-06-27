package ir.maktabsharif138.home_service_system.service.integration.email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplateBuilderTest {

    private final EmailTemplateBuilder builder =
            new EmailTemplateBuilder();

    @Test
    void buildVerificationEmail_shouldReplacePlaceholder() {

        String template =
                "Click here: {{verificationLink}}";

        String result =
                builder.buildVerificationEmail(
                        template,
                        "http://localhost/token"
                );

        assertEquals(
                "Click here: http://localhost/token",
                result
        );
    }

    @Test
    void buildVerificationEmail_shouldReturnTemplateWhenPlaceholderMissing() {

        String template = "hello world";

        String result =
                builder.buildVerificationEmail(
                        template,
                        "link"
                );

        assertEquals(
                "hello world",
                result
        );
    }
}