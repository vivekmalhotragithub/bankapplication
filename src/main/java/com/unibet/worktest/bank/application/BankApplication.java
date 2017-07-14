package com.unibet.worktest.bank.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot application that will initialize the {@link ApplicationContext}
 * required for BankFunctionalTest and other integration tests.
 * 
 *
 */
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.unibet.worktest.*" })
@ComponentScan(basePackages = { "com.unibet.worktest.*" })
@EntityScan(basePackages = { "com.unibet.worktest.*" })
@SpringBootApplication
public class BankApplication extends SpringApplication {

}
