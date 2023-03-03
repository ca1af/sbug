package com.sparta.sbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.Ordered;

@EnableCaching(order = Ordered.HIGHEST_PRECEDENCE)
@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
public class SbugApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	public static void main(String[] args) {
		SpringApplication.run(SbugApplication.class, args);
	}

}
