package ir.maktabsharif138.home_service_system.service.integration.email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplateLoaderTest {

    private final EmailTemplateLoader loader =
            new EmailTemplateLoader();

    @Test
    void loadVerificationTemplate_shouldLoadSuccessfully() {

        String template =
                loader.loadVerificationTemplate();

        assertNotNull(template);
        assertFalse(template.isBlank());
    }
}