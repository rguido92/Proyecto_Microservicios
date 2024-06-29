package com.microservicios.eurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RegisterServiceApplication {
	// http://localhost:8099
	public static void main(String[] args) {
		SpringApplication.run(RegisterServiceApplication.class, args);
	}
}
