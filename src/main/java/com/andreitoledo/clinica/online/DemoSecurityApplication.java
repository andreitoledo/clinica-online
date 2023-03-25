package com.andreitoledo.clinica.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoSecurityApplication {

	public static void main(String[] args) {
	//	System.out.println(new BCryptPasswordEncoder().encode("Admin@123"));
		SpringApplication.run(DemoSecurityApplication.class, args);
	}
}
