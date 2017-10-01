package com.surya.performancetest.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.surya")
public class PerformanceTestApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.location", "/config");
	    System.setProperty("spring.config.name", "config");
		SpringApplication.run(PerformanceTestApplication.class, args);
	}
}
