package com.unibet.worktest.bank.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot application
 * @author vivekmalhotra
 *
 */
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.unibet.worktest.bank.*" })
@ComponentScan(basePackages = { "com.unibet.worktest.bank.*" })
@EntityScan(basePackages = { "com.unibet.worktest.bank.*" })
@SpringBootApplication
public class BankApplication extends SpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
