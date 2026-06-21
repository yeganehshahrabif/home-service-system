package ir.maktabsharif138.home_service_system.service.integration.captcha;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CaptchaGenerator {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();

    public String generateText() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return sb.toString();
    }

    public String generateImage(String text) {
        try {

            BufferedImage image = new BufferedImage(120, 40, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = image.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 120, 40);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));

            g.drawString(text, 20, 28);

            g.dispose();

            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

                ImageIO.write(image, "png", output);

                return "data:image/png;base64,"
                        + Base64.getEncoder()
                        .encodeToString(output.toByteArray());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}