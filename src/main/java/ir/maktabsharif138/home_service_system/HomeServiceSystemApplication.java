package ir.maktabsharif138.home_service_system;

import ir.maktabsharif138.home_service_system.security.jwt.JwtProperties;
import ir.maktabsharif138.home_service_system.service.integration.email.EmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(JwtProperties.class)
public class HomeServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeServiceSystemApplication.class, args);

	}

}
