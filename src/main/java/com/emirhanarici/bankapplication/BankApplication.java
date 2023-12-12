package com.emirhanarici.bankapplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcConnectionDetails jdbc) {
		return args -> {
			System.out.println("URL: " + jdbc.getJdbcUrl());
			System.out.println("Username: " + jdbc.getUsername());
			System.out.println("Password: " + jdbc.getPassword());
		};

	}


}
