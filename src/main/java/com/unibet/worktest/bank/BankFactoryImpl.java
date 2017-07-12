/**
 * 
 */
package com.unibet.worktest.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * implementation of {@link BankFactory}.
 * 
 * @author vivekmalhotra
 *
 */
@Component
public class BankFactoryImpl implements BankFactory {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransferService transferService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.unibet.worktest.bank.BankFactory#getAccountService()
	 */
	@Override
	public AccountService getAccountService() {
		// return the account service
		return accountService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.unibet.worktest.bank.BankFactory#getTransferService()
	 */
	@Override
	public TransferService getTransferService() {
		// return the transfer service
		return transferService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.unibet.worktest.bank.BankFactory#setupInitialData()
	 */
	@Override
	public void setupInitialData() {
		//

	}

}
