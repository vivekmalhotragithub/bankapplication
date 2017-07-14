/**
 * 
 */
package com.unibet.worktest.bank.entity;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;

/**
 * 
 * Entity that represents an Account transaction
 *
 */
@Entity
@Table(name = "ACCOUNT_TRANSACTION", uniqueConstraints = @UniqueConstraint(columnNames = { "TRANSACTION_REF" }))
public class AccountTransaction {

	@Id
	@Column(name = "TRANSACTION_ID")
	@GeneratedValue(generator = "SEQ_TRANSACTION_ID", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "SEQ_TRANSACTION", allocationSize = 1, name = "SEQ_TRANSACTION_ID")
	private Long transactionId;

	@Column(name = "TRANSACTION_REF", nullable = false)
	private String transactionRef;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "DATE", columnDefinition = "DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transaction")
	private List<AccountTransactionLeg> transactionLegs;

	public AccountTransaction() {

	}

	/**
	 * Construct account transaction from a {@link TransferRequest} 
	 * @param transferRequest
	 */
	public AccountTransaction(TransferRequest transferRequest) {
		this.date = new Date();
		this.transactionRef = transferRequest.getReference();
		this.type = transferRequest.getType();
		this.transactionLegs = new ArrayList<>();
	}

	/**
	 * @return the transactionId
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the transactionRef
	 */
	public String getTransactionRef() {
		return transactionRef;
	}

	/**
	 * @param transactionRef
	 *            the transactionRef to set
	 */
	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the transactionLegs
	 */
	public List<AccountTransactionLeg> getTransactionLegs() {
		return transactionLegs;
	}

	/**
	 * @param transactionLegs
	 *            the transactionLegs to set
	 */
	public void setTransactionLegs(List<AccountTransactionLeg> transactionLegs) {
		this.transactionLegs = transactionLegs;
	}

	/**
	 * Convenience method to convert the entity to a Transaction
	 * 
	 * @return
	 */
	public Transaction toTransactionValueObject() {
		//
		List<TransactionLeg> transactionLegDTOList = new ArrayList<>();
		for (AccountTransactionLeg transactionLeg : this.transactionLegs) {
			transactionLegDTOList.add(transactionLeg.toTransactionLegValueObject());
		}
		return new Transaction(this.transactionRef, this.type, this.date, transactionLegDTOList);
	}

}
