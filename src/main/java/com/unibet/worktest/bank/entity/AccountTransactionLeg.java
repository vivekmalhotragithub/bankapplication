package com.unibet.worktest.bank.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author vivekmalhotra
 *
 */
@Entity
@Table(name = "TRANSACTION_LEG")
public class AccountTransactionLeg {

	@Id
	@GeneratedValue(generator = "SEQ_TRANSACTION_LEG_ID", strategy = GenerationType.SEQUENCE)
	@Column(name = "TRANS_LEG_ID")
	@SequenceGenerator(sequenceName = "SEQ_TRANSACTION_LEG", allocationSize = 1, name = "SEQ_TRANSACTION_LEG_ID")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACTION_ID")
	private AccountTransaction transaction;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_REF")
	private Account account;
	
	@Column(name = "AMOUNT")
	private BigDecimal amount;
	
	@Column(name = "CURRENCY")
	private String currency;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the transaction
	 */
	public AccountTransaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(AccountTransaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}


}
