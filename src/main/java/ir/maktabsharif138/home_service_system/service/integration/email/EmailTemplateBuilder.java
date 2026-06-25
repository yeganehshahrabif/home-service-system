package ir.maktabsharif138.home_service_system.service.integration.email;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildVerificationEmail(String template, String verificationLink) {

        return template.replace(
                "{{verificationLink}}",
                verificationLink
        );
    }
}