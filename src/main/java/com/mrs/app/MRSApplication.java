package com.mrs.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MRSApplication {

	public static void main(String[] args) {
		SpringApplication.run(MRSApplication.class, args);
	}

}
