package com.project.film;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 등록일 및 수정일 자동화를 위해 import
public class FilmProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmProjectApplication.class, args);
	}

}
