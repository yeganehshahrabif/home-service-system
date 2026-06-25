package ir.maktabsharif138.home_service_system.service.integration.email;

import ir.maktabsharif138.home_service_system.exception.EmailException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EmailTemplateLoader {

    public String loadVerificationTemplate() {

        try {

            ClassPathResource resource =
                    new ClassPathResource(
                            "templates/verification-email.html"
                    );

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (Exception ex) {

            throw new EmailException(
                    "Failed to load email template",
                    ex
            );
        }
    }
}