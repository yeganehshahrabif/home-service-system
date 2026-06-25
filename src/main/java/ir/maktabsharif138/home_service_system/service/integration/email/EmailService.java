package ir.maktabsharif138.home_service_system.service.integration.email;

public interface EmailService {

    void send(
            String to,
            String subject,
            String content
    );
}