package com.innoxIT.springbootApplication_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication

public class SpringbootApplicationProjectApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(SpringbootApplicationProjectApplication.class, args);
	}

}
