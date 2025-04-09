package com.example.bookapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BookapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookapiApplication.class, args);
	}

}
