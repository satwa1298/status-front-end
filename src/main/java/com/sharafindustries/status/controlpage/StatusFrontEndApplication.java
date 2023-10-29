package com.sharafindustries.status.controlpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StatusFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusFrontEndApplication.class, args);
	}

}
