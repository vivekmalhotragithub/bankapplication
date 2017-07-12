/**
 * 
 */
package com.unibet.worktest.bank.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;

/**
 * @author vivekmalhotra
 *
 */
@Transactional(readOnly = true)
@Service
public class TransferServiceImpl implements TransferService {

	/* (non-Javadoc)
	 * @see com.unibet.worktest.bank.TransferService#transferFunds(com.unibet.worktest.bank.TransferRequest)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	public void transferFunds(TransferRequest transferRequest) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.unibet.worktest.bank.TransferService#findTransactions(java.lang.String)
	 */
	@Override
	public List<Transaction> findTransactions(String accountRef) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.unibet.worktest.bank.TransferService#getTransaction(java.lang.String)
	 */
	@Override
	public Transaction getTransaction(String transactionRef) {
		// TODO Auto-generated method stub
		return null;
	}

}
