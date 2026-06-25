package ir.maktabsharif138.home_service_system.service.integration.email.impl;

import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailService;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailTemplateBuilder;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailTemplateLoader;
import ir.maktabsharif138.home_service_system.service.integration.email.VerificationEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationEmailServiceImpl
        implements VerificationEmailService {

    private final EmailService emailService;

    private final EmailTemplateLoader emailTemplateLoader;

    private final EmailTemplateBuilder emailTemplateBuilder;

    private final EmailVerificationTokenCoreService tokenCoreService;

    @Value("${app.email-verification-base-url}")
    private String verificationBaseUrl;

    @Override
    public void sendVerificationEmail(String email, Role role) {

        EmailVerificationToken verificationToken = tokenCoreService.createToken(email, role);

        String verificationLink = verificationBaseUrl + "?token=" + verificationToken.getToken();

        String template = emailTemplateLoader.loadVerificationTemplate();

        String content =
                emailTemplateBuilder
                        .buildVerificationEmail(
                                template,
                                verificationLink
                        );

        emailService.send(email, "Verify your email", content);
    }
}