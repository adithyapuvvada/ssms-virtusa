package com.ssms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsmsApplication.class, args);
	}

}
