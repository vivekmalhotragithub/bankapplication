/**
 * 
 */
package com.unibet.worktest.bank.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.unibet.worktest.bank.AccountAlreadyExistsException;
import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.dao.AccountRepository;
import com.unibet.worktest.bank.entity.Account;

/**
 * 
 * Implementation of {@link AccountService}
 * 
 * @author vivekmalhotra
 *
 */
@Service
public class AccountServiceImpl implements AccountService {

	// logger
	private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class);

	private AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.unibet.worktest.bank.AccountService#createAccount(java.lang.String,
	 * com.unibet.worktest.bank.Money)
	 */
	@Override
	public void createAccount(String accountRef, Money amount) throws AccountAlreadyExistsException {
		
		// create an Account object from account reference and money balance
		Account account = new Account(accountRef, amount);
		try {
			account = accountRepository.save(account);
		} catch (DataIntegrityViolationException ex) {
			// check if an account already exists
			LOGGER.error("Account already exists with ref " + accountRef);
			throw new AccountAlreadyExistsException(accountRef);
		}

		LOGGER.info("Account created with detail " + account);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.unibet.worktest.bank.AccountService#getAccountBalance(java.lang.
	 * String)
	 */
	@Override
	public Money getAccountBalance(String accountRef) {
		// get the account from repository based on account reference
		Account account = accountRepository.findByAccountRef(accountRef);
		// if account is null we throw AccountNotFoundException
		if (account == null) {
			LOGGER.error("Cannot find an Account with reference " + accountRef);
			throw new AccountNotFoundException(accountRef);
		}
		return account.getBalance();
	}

}
