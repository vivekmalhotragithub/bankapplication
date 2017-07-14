/**
 * 
 */
package com.unibet.worktest.bank;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * implementation of {@link BankFactory}.
 * 
 * @author vivekmalhotra
 *
 */
@Component
public class BankFactoryImpl implements BankFactory, ApplicationContextAware{


	@Autowired
	private static ApplicationContext applicationContext;
	

	@Override
	public AccountService getAccountService() {
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
		BankFactoryImpl.applicationContext = applicationContext;
	}

}
