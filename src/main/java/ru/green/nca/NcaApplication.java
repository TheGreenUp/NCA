package ru.green.nca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class NcaApplication {
	public static void main(String[] args) {
		SpringApplication.run(NcaApplication.class, args);
	}

}
