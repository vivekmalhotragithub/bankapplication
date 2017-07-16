/**
 * 
 */
package com.unibet.worktest.bank.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.BankFunctionalTest;
import com.unibet.worktest.bank.Sealed;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;
import com.unibet.worktest.bank.UnbalancedLegsException;
import com.unibet.worktest.bank.application.BankApplication;
import com.unibet.worktest.bank.util.BankTestUtil;

/**
 * Integration tests for TransferService. This test covers more scenarios than
 * what are covered in {@link BankFunctionalTest}
 * 
 * @author vivekmalhotra
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@Sealed
public class TransferServiceTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransferService transferService;

	@Before
	public void setup() {
		accountService.createAccount("TRANSFER:1:EUR", BankTestUtil.MONEY1200EUR);
		accountService.createAccount("TRANSFER:2:EUR", BankTestUtil.MONEY1200EUR);
	}

	// testing the scenario when net transaction is not balanced
	@Test(expected = UnbalancedLegsException.class)
	public void shouldFailIfTransactionRequestIsNotBalanced() {
		transferService.transferFunds(TransferRequest.builder().reference("tx1").account("TRANSFER:1:EUR")
				.amount(BankTestUtil.MONEY_ADD_100EUR).account("TRANSFER:2:EUR").amount(BankTestUtil.MONEY_ADD_100EUR)
				.build());
	}

	@Test(expected = AccountNotFoundException.class)
	public void shouldFailIfAnyAccountDoesnotExist() {
		transferService.transferFunds(TransferRequest.builder().reference("tx1").account("TRANSFER:4:EUR")
				.amount(BankTestUtil.MONEY_ADD_100EUR).account("TRANSFER:2:EUR").amount(BankTestUtil.MONEY_DEDUCT_100EUR)
				.build());
	}

	@Test(expected = NullPointerException.class)
	public void shouldFailIfTransaferRequestIsNull() {
		transferService.transferFunds(null);
	}

}
