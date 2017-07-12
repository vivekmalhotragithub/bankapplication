package com.unibet.worktest.bank.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.unibet.worktest.bank.application.BankApplication;
import com.unibet.worktest.bank.entity.Account;
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
public class AccountRepositoryTest {

	@Autowired
	AccountRepository accountRepository;

	@Before
	public void setup() {
		accountRepository.save(new Account("CASH:1:EUR", BankTestUtil.MONEY1200EUR));
		accountRepository.flush();
	}

	@Test
	public void shouldSaveAccountSuccessfully() {
		// when
		Account account = new Account("CASH:2:USD", BankTestUtil.MONEY1000USD);
		account = accountRepository.save(account);

		// then
		Assert.assertNotNull(account.getAccountId());
		Assert.assertEquals(2, accountRepository.findAll().size());
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void shouldNotSaveAccountWithExistingAccountRef() {

		Account account = new Account("CASH:1:EUR", BankTestUtil.MONEY1200EUR);
		account = accountRepository.save(account);
		accountRepository.flush();
	}

	@Test
	public void shouldReturnAccountWithExistingAccountRef() {

		Account account = accountRepository.findByAccountRef("CASH:1:EUR");
		Assert.assertNotNull(account);
	}

	@Test
	public void shouldReturnNullWithInvalidAccountRef() {

		Account account = accountRepository.findByAccountRef("CASH:4:EUR");
		Assert.assertNull(account);
	}

}
