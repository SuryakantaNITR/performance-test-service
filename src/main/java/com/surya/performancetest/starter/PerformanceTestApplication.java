package com.surya.performancetest.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.surya")
public class PerformanceTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerformanceTestApplication.class, args);
	}
}
