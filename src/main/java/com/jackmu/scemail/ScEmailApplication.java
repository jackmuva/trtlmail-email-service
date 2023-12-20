package com.jackmu.scemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScEmailApplication.class, args);
	}

}
