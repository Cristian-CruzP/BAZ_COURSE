package com.wizeline;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.wizeline.repository.UserRepository;
import com.wizeline.repository.UserRepositoryImpl;
import com.wizeline.service.UserService;
import com.wizeline.service.UserServiceImpl;

@SpringBootApplication
public class LearningjavagradleApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningjavagradleApplication.class, args);
	}

}