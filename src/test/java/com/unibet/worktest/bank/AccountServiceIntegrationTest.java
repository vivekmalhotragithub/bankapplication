package com.unibet.worktest.bank;

import java.util.Currency;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.unibet.worktest.bank.application.BankApplication;
import com.unibet.worktest.bank.service.AccountServiceImpl;

/**
 * Integration test of {@link AccountServiceImpl}
 * 
 * @author vivekmalhotra
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = BankApplication.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AccountServiceIntegrationTest {

	private final Currency EURO = Currency.getInstance("EUR");

	private final Currency USD = Currency.getInstance("USD");

	private final Money MONEY1 = Money.create("1200", EURO);

	private final Money MONEY2 = Money.create("100", USD);

	@Autowired
	private AccountService accountService;

	@Before
	public void setup() {
		// BankTestUtil.insertBasicAccounts(accountService);
	}

}
