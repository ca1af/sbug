package com.sparta.sbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SbugApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbugApplication.class, args);
	}

}
