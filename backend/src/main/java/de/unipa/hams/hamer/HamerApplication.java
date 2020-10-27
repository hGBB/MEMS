package de.unipa.hams.hamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HamerApplication.class, args);
	}
}
