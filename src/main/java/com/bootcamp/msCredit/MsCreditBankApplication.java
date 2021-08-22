package com.bootcamp.msCredit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * The type Ms credit bank application.
 */
@SpringBootApplication
@EnableEurekaClient
public class MsCreditBankApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsCreditBankApplication.class, args);
	}

}
