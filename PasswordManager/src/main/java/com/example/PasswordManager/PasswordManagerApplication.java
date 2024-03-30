package com.example.PasswordManager;

import com.example.PasswordManager.repository.PasswordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordManagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}

	@Autowired
	private PasswordRepo passwordRepo;

	@Override
	public void run(String... args) throws Exception {

	}
}
