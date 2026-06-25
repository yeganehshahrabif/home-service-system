package ir.maktabsharif138.home_service_system.service.integration.email.impl;

import ir.maktabsharif138.home_service_system.exception.EmailException;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailtrapEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void send(String to, String subject, String content) {

        try {
            var message = mailSender.createMimeMessage();

            var helper = new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception ex) {
            throw new EmailException("Failed to send email", ex);
        }
    }
}