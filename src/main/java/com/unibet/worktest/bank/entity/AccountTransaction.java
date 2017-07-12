/**
 * 
 */
package com.unibet.worktest.bank.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * 
 * Entity that represents an Account transaction
 * 
 * @author vivekmalhotra
 *
 */
@Entity
@Table(name = "ACCOUNT_TRANSACTION", uniqueConstraints = @UniqueConstraint(columnNames = { "TRANSACTION_REF" }))
public class AccountTransaction {

	@Id
	@Column(name = "TRANSACTION_ID")
	@GeneratedValue(generator = "SEQ_TRANSACTION_ID", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "SEQ_TRANSACTION", allocationSize = 1, name = "SEQ_TRANSACTION_ID")
	private int transactionId;

	@Column(name = "TRANSACTION_REF")
	private String transactionRef;

	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "DATE", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transaction")
	private List<AccountTransactionLeg> transactionLegs;

	/**
	 * @return the transactionId
	 */
	public int getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(int transactionId) {
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

}
