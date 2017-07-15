/**
 * 
 */
package com.unibet.worktest.bank.service;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.AccountAlreadyExistsException;
import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.dao.AccountRepository;
import com.unibet.worktest.bank.entity.Account;

/**
 * 
 * Implementation of {@link AccountService}. This is a spring @Service and will
 * create a read only transaction for all its methods by default.
 * 
 * @author vivekmalhotra
 *
 */
@Service("accountService")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

	// logger
	private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class);

	private AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**
	 * Method to create an account with provided reference and initial balance
	 * denoted by {@link Money}. it will create a modifying transaction
	 * 
	 * @param accountRef
	 *            account reference
	 * @param amount
	 *            initial balance of Account as {@link Money}
	 * @throws NullPointerException
	 *             if one of required parameters is null
	 * @throws AccountAlreadyExistsException
	 *             if account with same reference already exists
	 */
	@Override
	@Transactional(readOnly = false)
	public void createAccount(String accountRef, Money amount) throws AccountAlreadyExistsException {
		Validate.notNull(accountRef, "Please provide a valid account reference.");
		Validate.notNull(amount, "Please provide a valid amount.");
		Validate.notNull(amount.getAmount(), "Please provide a valid amount.");
		Validate.notNull(amount.getCurrency(), "Please provide a valid currency.");
		Validate.isTrue(amount.getAmount().signum() >= 0, "Cannot create account with negative balance.");
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

	/**
	 * Method to get account balance based on account reference.
	 * 
	 * @param accountRef
	 *            account reference
	 * @return balance in terms of {@link Money}
	 * @throws NullPointerException
	 *             if accountRef is null
	 * @throws AccountNotFoundException
	 *             account not found based on account ref
	 * 
	 */
	@Override
	public Money getAccountBalance(String accountRef) {
		Validate.notNull(accountRef, "Please provide a valid account reference.");
		// get the account from repository based on account reference
		Account account = accountRepository.findByAccountRef(accountRef);
		// if account is null we throw AccountNotFoundException
		if (account == null) {
			LOGGER.error("Cannot find an Account with reference " + accountRef);
			throw new AccountNotFoundException(accountRef);
		}
		LOGGER.info("account found with reference " + accountRef + "and details " + account);
		return account.getBalance();
	}

}
