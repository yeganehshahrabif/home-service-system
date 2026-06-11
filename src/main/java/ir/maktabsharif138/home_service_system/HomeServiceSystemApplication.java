package ir.maktabsharif138.home_service_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HomeServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeServiceSystemApplication.class, args);
	}

}
