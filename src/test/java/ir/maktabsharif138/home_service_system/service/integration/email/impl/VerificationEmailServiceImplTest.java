package ir.maktabsharif138.home_service_system.service.integration.email.impl;

import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailService;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailTemplateBuilder;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailTemplateLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationEmailServiceImplTest {

    @Mock
    private EmailService emailService;

    @Mock
    private EmailTemplateLoader templateLoader;

    @Mock
    private EmailTemplateBuilder templateBuilder;

    @Mock
    private EmailVerificationTokenCoreService tokenCoreService;

    @InjectMocks
    private VerificationEmailServiceImpl service;

    @BeforeEach
    void setup() {

        ReflectionTestUtils.setField(
                service,
                "verificationBaseUrl",
                "http://localhost:8080/verify"
        );
    }

    @Test
    void sendVerificationEmail_shouldSendSuccessfully() {

        EmailVerificationToken token =
                EmailVerificationToken.builder()
                        .token("abc123")
                        .build();

        when(tokenCoreService.createToken(
                "test@test.com",
                Role.CUSTOMER
        )).thenReturn(token);

        when(templateLoader.loadVerificationTemplate())
                .thenReturn("template");

        when(templateBuilder.buildVerificationEmail(
                anyString(),
                anyString()
        )).thenReturn("content");

        service.sendVerificationEmail(
                "test@test.com",
                Role.CUSTOMER
        );

        verify(tokenCoreService)
                .createToken(
                        "test@test.com",
                        Role.CUSTOMER
                );

        verify(templateLoader)
                .loadVerificationTemplate();

        verify(templateBuilder)
                .buildVerificationEmail(
                        "template",
                        "http://localhost:8080/verify?token=abc123"
                );

        verify(emailService)
                .send(
                        "test@test.com",
                        "Verify your email",
                        "content"
                );
    }
}