package com.danielpm1982.spring_security_demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}
}

/*
After building and starting this application:
-> you can view and test all endpoints at:
http://localhost:8080/swagger
or
http://localhost:8080/v3/api-docs

-> you can view the H2 console at:
http://localhost:8080/h2-console
*/
