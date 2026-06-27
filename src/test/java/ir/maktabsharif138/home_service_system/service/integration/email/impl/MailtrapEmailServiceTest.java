package ir.maktabsharif138.home_service_system.service.integration.email.impl;

import ir.maktabsharif138.home_service_system.exception.EmailException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailtrapEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailtrapEmailService service;

    @Test
    void send_shouldSendSuccessfully() {

        when(mailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        service.send(
                "test@test.com",
                "subject",
                "content"
        );

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void send_shouldThrowEmailException() {

        when(mailSender.createMimeMessage())
                .thenThrow(new RuntimeException());

        assertThrows(
                EmailException.class,
                () -> service.send(
                        "test@test.com",
                        "subject",
                        "content"
                )
        );
    }
}