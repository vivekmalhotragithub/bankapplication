package com.unibet.worktest.bank.entity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.util.BankTestUtil;

/**
 * Unit tests for {@link AccountTransaction}
 *
 */
public class AccountTransactionTest {
	private Account account1;
	private Account account2;

	private AccountTransaction transaction;

	private AccountTransactionLeg transactionLeg1;
	private AccountTransactionLeg transactionLeg2;

	private TransactionLeg transactionlegvo1;
	private TransactionLeg transactionlegvo2;

	private Date transactionDate;

	@Before
	public void Setup() {
		transaction = new AccountTransaction();

		account1 = Mockito.mock(Account.class);
		transactionLeg1 = Mockito.mock(AccountTransactionLeg.class);

		account2 = Mockito.mock(Account.class);
		transactionLeg2 = Mockito.mock(AccountTransactionLeg.class);

		transactionlegvo1 = new TransactionLeg("TRANSFER:1:EUR", BankTestUtil.MONEY_ADD_100EUR);
		transactionlegvo2 = new TransactionLeg("TRANSFER:2:EUR", BankTestUtil.MONEY_DEDUCT_100EUR);

		Mockito.when(account1.getAccountRef()).thenReturn("TRANSFER:1:EUR");
		Mockito.when(account2.getAccountRef()).thenReturn("TRANSFER:2:EUR");
		Mockito.when(transactionLeg1.getTransaction()).thenReturn(transaction);
		Mockito.when(transactionLeg2.getTransaction()).thenReturn(transaction);
		Mockito.when(transactionLeg1.getAccount()).thenReturn(account1);
		Mockito.when(transactionLeg2.getAccount()).thenReturn(account2);
		Mockito.when(transactionLeg1.toTransactionLegValueObject()).thenReturn(transactionlegvo1);
		Mockito.when(transactionLeg2.toTransactionLegValueObject()).thenReturn(transactionlegvo2);
		transactionDate = Calendar.getInstance().getTime();
	}

	@Test
	public void getterSetterShouldWork() {
		transaction.setTransactionRef("TX1");
		transaction.setDate(transactionDate);
		transaction.setTransactionLegs(Arrays.asList(transactionLeg1, transactionLeg2));

		// then
		Assert.assertEquals("TX1", transaction.getTransactionRef());
		Assert.assertEquals(transactionDate, transaction.getDate());
		Assert.assertEquals(Arrays.asList(transactionLeg1, transactionLeg2), transaction.getTransactionLegs());

	}

	@Test
	public void shouldConvertToValueObjectCorrectly() {
		transaction.setTransactionRef("TX1");
		transaction.setDate(transactionDate);
		transaction.setTransactionLegs(Arrays.asList(transactionLeg1, transactionLeg2));

		// then
		Transaction transactionVO = transaction.toTransactionValueObject();

		// then
		Assert.assertEquals("TX1", transactionVO.getReference());
		Assert.assertEquals(transactionDate, transactionVO.getDate());
		Assert.assertTrue(transactionVO.getLegs().contains(transactionlegvo1));
		Assert.assertTrue(transactionVO.getLegs().contains(transactionlegvo2));

	}

}
