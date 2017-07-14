package com.unibet.worktest.bank.entity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.unibet.worktest.bank.InsufficientFundsException;
import com.unibet.worktest.bank.Money;

/**
 * Entity that represents an Account.
 * 
 *
 */
@Entity
@Table(name = "ACCOUNT", uniqueConstraints = @UniqueConstraint(columnNames = { "ACCOUNT_REF" }))
public class Account {

	@Id
	@GeneratedValue(generator = "SEQ_ACCOUNT_ID", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "SEQUENCE_ACCOUNT", allocationSize = 1, name = "SEQ_ACCOUNT_ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ACCOUNT_ID")
	private Long accountId;

	@Column(name = "ACCOUNT_REF", nullable = false)
	private String accountRef;

	@Column(name = "ACCOUNT_BALANCE", nullable = false)
	private BigDecimal accountBalance;

	@Column(name = "CURRENCY", nullable = false)
	private String currency;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	private List<AccountTransactionLeg> transactionLegs;

	public Account() {

	}

	/**
	 * To construct an Account using an account reference and {@link Money}
	 * 
	 * @param accountRef
	 * @param money
	 */
	public Account(String accountRef, Money money) {
		this.accountRef = accountRef;
		this.accountBalance = money.getAmount();
		this.currency = money.getCurrency().getCurrencyCode();
	}

	/**
	 * Method to get the account balance
	 * 
	 * @return an instance of {@link Money}
	 */
	public Money getBalance() {
		Currency currency = Currency.getInstance(this.currency);
		return new Money(this.accountBalance, currency);
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountRef
	 */
	public String getAccountRef() {
		return accountRef;
	}

	/**
	 * @param accountRef
	 *            the accountRef to set
	 */
	public void setAccountRef(String accountRef) {
		this.accountRef = accountRef;
	}

	/**
	 * @return the accountBalance
	 */
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @param accountBalance
	 *            the accountBalance to set
	 */
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the allTransactions
	 */
	public List<AccountTransactionLeg> getTransactionLegs() {
		return transactionLegs;
	}

	/**
	 * @param allTransactions
	 *            the allTransactions to set
	 */
	public void setTransactionLegs(List<AccountTransactionLeg> transactionLegs) {
		this.transactionLegs = transactionLegs;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Account");
		builder.append("[ id = " + this.accountId);
		builder.append(", accountRef = " + this.accountRef);
		builder.append(", balance = " + this.accountBalance + " " + this.currency);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * For hashCode we only consider the account Id and account reference
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		int hashCode = 1;
		hashCode = 37 * hashCode + (int) (this.accountId ^ (this.accountId >>> 32));
		hashCode = 37 * hashCode + this.accountRef.hashCode();
		return hashCode;
	}

	/**
	 * This method performs a transaction on an Account. It first checks if the
	 * transaction will lead to negative balance. Successful transactions are carried out.
	 * 
	 * @throws InsufficientFundsException
	 */
	public void performTransaction(BigDecimal transactionAmount) {
		BigDecimal balance = this.accountBalance.add(transactionAmount);
		// if account balance will be negative on performing the transaction we
		// throw exception
		if (balance.signum() < 0) {
			throw new InsufficientFundsException(this.accountRef);
		}
		this.accountBalance = balance;
	}

}
