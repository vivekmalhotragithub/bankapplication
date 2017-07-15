/**
 * 
 */
package com.unibet.worktest.bank;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * It implements {@link BankFactory" and ApplicationContextAware interface. It
 * implements the setApplicationContext(ApplicationContext appContext) method to
 * set static instance applicationContext. This is then used to get the
 * accountService and transferService bean.
 * 
 * @author vivekmalhotra
 *
 */
@Component
public class BankFactoryImpl implements BankFactory, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	/**
	 * get the accountService bean from the application context
	 */
	@Override
	public AccountService getAccountService() {
		return (AccountService) applicationContext.getBean("accountService");
	}

	/**
	 * get the transferService bean from the application context
	 */
	@Override
	public TransferService getTransferService() {
		return (TransferService) applicationContext.getBean("transferService");
	}

	@Override
	public void setupInitialData() {

	}

	/**
	 * callback to set the application context.
	 * 
	 * @param applicationContext
	 * @throws BeansException
	 *             if thrown by application context methods
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BankFactoryImpl.applicationContext = applicationContext;
	}

}
