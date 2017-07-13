/**
 * 
 */
package com.unibet.worktest.bank;

import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 
 * implementation of {@link BankFactory}.
 * 
 * @author vivekmalhotra
 *
 */
@Service
public class BankFactoryImpl implements BankFactory, ApplicationContextAware {

	private ApplicationContext applicationContext = null;

	@Override
	public AccountService getAccountService() {
		new SpringBootTestContextBootstrapper().buildTestContext();
		return (AccountService) applicationContext.getBean("accountService");
	}

	@Override
	public TransferService getTransferService() {
		return (TransferService) applicationContext.getBean("transferService");
	}

	@Override
	public void setupInitialData() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
