package com.unibet.worktest.bank.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.unibet.worktest.bank.AccountAlreadyExistsException;
import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.BankFunctionalTest;
import com.unibet.worktest.bank.Sealed;
import com.unibet.worktest.bank.application.BankApplication;
import com.unibet.worktest.bank.util.BankTestUtil;

/**
 * Integration tests for {@link AccountServiceImpl}. The scenarios covered here
 * are different from the one covered in {@link BankFunctionalTest}
 * 
 * @author vivekmalhotra
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@Sealed
public class AccountServiceTest {

	@Autowired
	private AccountService accountService;

	@Before
	public void setup() {
		accountService.createAccount("TRANSFER:1:EUR", BankTestUtil.MONEY1200EUR);
	}

	@Test(expected = AccountAlreadyExistsException.class)
	public void shouldFailToCreateAccountIfAlreadyExists() {
		accountService.createAccount("TRANSFER:1:EUR", BankTestUtil.MONEY1000USD);
	}

	@Test(expected = AccountNotFoundException.class)
	public void shouldFailToFetchBalanceIfAccountDoesNotExist() {
		accountService.getAccountBalance("TRANSFER:2:EUR");
	}

}
