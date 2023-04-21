package com.example.bloglv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class BlogLv3Application {

	public static void main(String[] args) {
		SpringApplication.run(BlogLv3Application.class, args);
	}

}
