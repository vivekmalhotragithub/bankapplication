package com.unibet.worktest.bank.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
		Assert.assertTrue(Arrays.asList(transaction.getTransactionLegs().get(0).getAccount(),
				transaction.getTransactionLegs().get(1).getAccount()).contains(account1));
		Assert.assertTrue(Arrays.asList(transaction.getTransactionLegs().get(0).getAccount(),
				transaction.getTransactionLegs().get(1).getAccount()).contains(account2));

	}

	@Test
	@Rollback(false)
	public void shouldGetAllTransactionsForAccountRef() {
		performTransaction();
		List<AccountTransaction> transList = transactionRepository.findByAccountRef(account1.getAccountRef());

		// then
		Assert.assertEquals(1, transList.size());
		AccountTransaction transaction = transList.get(0);
		Assert.assertEquals("tx1", transaction.getTransactionRef());
		Assert.assertEquals(2, transaction.getTransactionLegs().size());
		Assert.assertTrue(Arrays.asList(transaction.getTransactionLegs().get(0).getAccount(),
				transaction.getTransactionLegs().get(1).getAccount()).contains(account1));
		Assert.assertTrue(Arrays.asList(transaction.getTransactionLegs().get(0).getAccount(),
				transaction.getTransactionLegs().get(1).getAccount()).contains(account2));

	}

	@Test
	public void shouldReturnNullForInvalidAccountRef() {
		performTransaction();
		List<AccountTransaction> transList = transactionRepository.findByAccountRef("abcdef");

		// then
		Assert.assertNull(transList);
	}

	@Test
	public void shouldGetTransactionByTransactionRef() {
		performTransaction();
		AccountTransaction transaction = transactionRepository.findByTransactionRef("tx1");

		// then
		Assert.assertEquals("tx1", transaction.getTransactionRef());
		Assert.assertEquals(2, transaction.getTransactionLegs().size());
		Assert.assertTrue(Arrays.asList(transaction.getTransactionLegs().get(0).getAccount(),
				transaction.getTransactionLegs().get(1).getAccount()).contains(account1));
		Assert.assertTrue(Arrays.asList(transaction.getTransactionLegs().get(0).getAccount(),
				transaction.getTransactionLegs().get(1).getAccount()).contains(account2));

	}

	@Test
	public void shouldReturnNullForInvalidTransactionRef() {
		performTransaction();
		AccountTransaction transaction = transactionRepository.findByTransactionRef("abcdef");

		// then
		Assert.assertNull(transaction);
	}

	private AccountTransaction performTransaction() {
		AccountTransaction transaction = new AccountTransaction();
		transaction.setTransactionRef("tx1");
		transaction.setDate(new Date());
		transaction.setType("CASH");
		AccountTransactionLeg leg1 = new AccountTransactionLeg();
		leg1.setAccount(account1);
		leg1.setAmount(BankTestUtil.MONEY_ADD_100EUR.getAmount());
		leg1.setCurrency(BankTestUtil.MONEY_ADD_100EUR.getCurrency().getCurrencyCode());
		leg1.setTransaction(transaction);

		AccountTransactionLeg leg2 = new AccountTransactionLeg();
		leg2.setAccount(account1);
		leg2.setAmount(BankTestUtil.MONEY_DEDUCT_100EUR.getAmount());
		leg2.setCurrency(BankTestUtil.MONEY_DEDUCT_100EUR.getCurrency().getCurrencyCode());
		List<AccountTransactionLeg> legs = new ArrayList<>();
		legs.add(leg1);
		legs.add(leg2);
		leg2.setTransaction(transaction);

		transaction.setTransactionLegs(legs);

		// when
		return transactionRepository.save(transaction);
	}

}
