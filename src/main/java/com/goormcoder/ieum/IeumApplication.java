package com.goormcoder.ieum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IeumApplication {

	public static void main(String[] args) {
		SpringApplication.run(IeumApplication.class, args);
	}

}
