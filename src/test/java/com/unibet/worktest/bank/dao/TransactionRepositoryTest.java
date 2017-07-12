package com.unibet.worktest.bank.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.unibet.worktest.bank.application.BankApplication;
import com.unibet.worktest.bank.entity.Account;
import com.unibet.worktest.bank.entity.AccountTransaction;
import com.unibet.worktest.bank.entity.AccountTransactionLeg;
import com.unibet.worktest.bank.util.BankTestUtil;

/**
 * Tests for {@link AccountRepository}
 * 
 * @author vivekmalhotra
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class TransactionRepositoryTest {

	private Account account1 = new Account("CASH:1:EUR", BankTestUtil.MONEY1200EUR);

	private Account account2 = new Account("CASH:2:EUR", BankTestUtil.MONEY1200EUR);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Before
	public void setup() {
		account1 = accountRepository.save(account1);
		account2 = accountRepository.save(account2);
		accountRepository.flush();
	}

	@Test
	public void shouldSaveTransactionsSuccessfully() {

		// when
		AccountTransaction transaction = performTransaction();

		// then
		Assert.assertNotNull(transaction.getTransactionId());
		Assert.assertEquals(2, transaction.getTransactionLegs().size());
		Assert.assertEquals(account1, transaction.getTransactionLegs().get(0).getAccount());

	}

	@Test
	public void shouldGetAllTransactionsForAccountRef() {
		performTransaction();
		List<AccountTransaction> trans = transactionRepository.findByAccounRef(account1.getAccountRef());

		// then
		Assert.assertEquals(2, trans.size());

	}

	private AccountTransaction performTransaction() {
		AccountTransaction transaction = new AccountTransaction();
		transaction.setTransactionRef("tx1");
		AccountTransactionLeg leg1 = new AccountTransactionLeg();
		leg1.setAccount(account1);
		leg1.setAmount(BankTestUtil.MONEY_ADD_100EUR.getAmount());
		leg1.setCurrency(BankTestUtil.MONEY_ADD_100EUR.getCurrency().getCurrencyCode());

		AccountTransactionLeg leg2 = new AccountTransactionLeg();
		leg2.setAccount(account1);
		leg2.setAmount(BankTestUtil.MONEY_DEDUCT_100EUR.getAmount());
		leg2.setCurrency(BankTestUtil.MONEY_DEDUCT_100EUR.getCurrency().getCurrencyCode());
		List<AccountTransactionLeg> legs = new ArrayList<>();
		legs.add(leg1);
		legs.add(leg2);

		transaction.setTransactionLegs(legs);

		// when
		return transactionRepository.save(transaction);
	}

}
