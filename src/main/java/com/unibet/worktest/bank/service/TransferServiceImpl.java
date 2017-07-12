/**
 * 
 */
package com.unibet.worktest.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.InsufficientFundsException;
import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;
import com.unibet.worktest.bank.UnbalancedLegsException;
import com.unibet.worktest.bank.dao.AccountRepository;
import com.unibet.worktest.bank.dao.TransactionRepository;
import com.unibet.worktest.bank.entity.Account;
import com.unibet.worktest.bank.entity.AccountTransaction;
import com.unibet.worktest.bank.entity.AccountTransactionLeg;

/**
 * 
 * An implementation for {@link TransferService}.
 * 
 * @author vivekmalhotra
 *
 */
@Transactional(readOnly = true)
@Service
public class TransferServiceImpl implements TransferService {

	private TransactionRepository transactionRepository;

	private AccountRepository accountRepository;

	@Autowired
	public TransferServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

	/**
	 * Perform a multi-legged transaction on 2 or more accounts
	 * @param transferRequest an instance of {@link TransferRequest} which defines multi-leged transactions on two or more accounts
	 * @throws NullPointerException
	 * @throws IllegalArgumentException if there are less than 2 {@link TransactionLeg} in transfer request
	 * @throws AccountNotFoundException if one of the accounts in transaction leg does not exist
	 * @throws UnbalancedLegsException if net transaction for each currency is not zero
	 * @throws InsufficientFundsException if there are not enough balance in account to carry out the transaction
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	public void transferFunds(TransferRequest transferRequest) {
		// validations
		Validate.notNull(transferRequest, "Please provide a valid transfer request.");
		Validate.notNull(transferRequest.getReference(), "Please provide a transfer reference.");
		Validate.notNull(transferRequest.getType(), "Please provide a transfer type.");
		Validate.notNull(transferRequest.getTransactionLegs(), "Provide atleast 2 transaction legs");
		Validate.isTrue(transferRequest.getTransactionLegs().size() > 2, "Provide atleast 2 transaction legs");

		// check if all the transaction legs in the transaction reference
		// balanced
		checkIfTransactionsAreBalanced(transferRequest.getTransactionLegs());

		// save transaction in repository
		AccountTransaction transactionEntity = new AccountTransaction(transferRequest);
		// update accounts with balance
		Map<String, BigDecimal> accountToNetTransactionMap = getNetTransactionByAccountRef(
				transferRequest.getTransactionLegs());
		List<AccountTransactionLeg> transactionLegsToSave = new ArrayList<>();
		for (Entry<String, BigDecimal> accountToNetTransaction : accountToNetTransactionMap.entrySet()) {
			String accReference = accountToNetTransaction.getKey();

			Account account = accountRepository.findByAccountRef(accReference);
			// if account is not found for account reference in transaction leg
			if (account == null) {
				throw new AccountNotFoundException(accReference);
			}
			// perform the net transaction on account
			account.performTransaction(accountToNetTransaction.getValue());
			// update the account in the repository
			accountRepository.save(account);
			// get all transaction leg entity list for the account
			List<AccountTransactionLeg> transactionLegEntityList = filterTransactionLegsByAccount(account,
					transactionEntity, transferRequest.getTransactionLegs());
			transactionLegsToSave.addAll(transactionLegEntityList);
		}

		// update the transaction
		transactionEntity.setTransactionLegs(transactionLegsToSave);
		transactionRepository.save(transactionEntity);

	}

	// Convenience method to check if transaction legs are balanced.
	// This method sums all the transaction amounts for each currency and if it
	// is equal to
	// zero then they are balanced.
	private void checkIfTransactionsAreBalanced(List<TransactionLeg> transactionLegList) {
		Map<String, BigDecimal> currencyToMoneyMap = new HashMap<>();

		// iterate over all transaction legs
		for (TransactionLeg transactionLeg : transactionLegList) {
			Objects.requireNonNull(transactionLeg.getAmount(), "Transaction Leg is missing Amount.");
			Objects.requireNonNull(transactionLeg.getAmount().getAmount(), "Transaction Leg is missing Amount.");
			Objects.requireNonNull(transactionLeg.getAmount().getCurrency(), "Transaction Leg is missing Currency.");
			String currencyCode = transactionLeg.getAmount().getCurrency().getCurrencyCode();
			// if there is already amount for currency
			if (currencyToMoneyMap.containsKey(currencyCode)) {
				// add transaction amount to the currency amount already present
				currencyToMoneyMap.put(currencyCode,
						currencyToMoneyMap.get(currencyCode).add(transactionLeg.getAmount().getAmount()));
			} else {
				currencyToMoneyMap.put(currencyCode, transactionLeg.getAmount().getAmount());
			}
		}

		// check if Total money for each currency is zero else throw
		// UnbalancedLegsException
		for (Entry<String, BigDecimal> currencyToMoneyEntry : currencyToMoneyMap.entrySet()) {
			if (currencyToMoneyEntry.getValue().signum() != 0) {
				throw new UnbalancedLegsException(
						"Transaction legs are not balanced for currency " + currencyToMoneyEntry.getKey()
								+ ", missing transaction of " + currencyToMoneyEntry.getValue().abs());
			}
		}

	}

	// this method gets the net Transaction by Account reference in all the
	// transaction legs
	private Map<String, BigDecimal> getNetTransactionByAccountRef(List<TransactionLeg> transactionLegList) {

		Map<String, BigDecimal> accountToNetTransactionMap = new HashMap<>();
		for (TransactionLeg transactionLeg : transactionLegList) {
			String accountReference = transactionLeg.getAccountRef();
			Validate.notNull(accountReference, "Please provide account reference for Transaction Leg");
			// if there is already amount for account reference
			if (accountToNetTransactionMap.containsKey(accountReference)) {
				// add transaction amount to the account ref already present
				accountToNetTransactionMap.put(accountReference,
						accountToNetTransactionMap.get(accountReference).add(transactionLeg.getAmount().getAmount()));
			} else {
				accountToNetTransactionMap.put(accountReference, transactionLeg.getAmount().getAmount());
			}
		}

		return accountToNetTransactionMap;
	}

	// Iterate over transaction legs and create corresponding
	// AccountTransactionLeg list
	private List<AccountTransactionLeg> filterTransactionLegsByAccount(Account account,
			AccountTransaction transactionEntity, List<TransactionLeg> transactionLegList) {
		List<AccountTransactionLeg> legsEntityList = new ArrayList<>();
		for (TransactionLeg transactionLeg : transactionLegList) {
			if (account.getAccountRef().equals(transactionLeg.getAccountRef())) {
				legsEntityList.add(new AccountTransactionLeg(account, transactionLeg, transactionEntity));
			}

		}
		return legsEntityList;
	}

	/**
	 * This method finds the transactions for an account and returns a list of
	 * {@link Transaction} value object.
	 * 
	 * @param accountRef
	 *            account reference
	 * @throws NullPointerException
	 *             if accountRef is null
	 */
	@Override
	public List<Transaction> findTransactions(String accountRef) {
		//
		Validate.notNull(accountRef, "Please provide a valid account reference.");
		List<AccountTransaction> transactions = transactionRepository.findByAccountRef(accountRef);
		return mapToTransactionValueObjectList(transactions);
	}

	// convert transactions to value object
	private List<Transaction> mapToTransactionValueObjectList(List<AccountTransaction> transactions) {
		List<Transaction> transactionDTOList = new ArrayList<>();
		for (AccountTransaction accTransaction : transactions) {
			transactionDTOList.add(accTransaction.toTransactionValueObject());
		}
		return transactionDTOList;
	}

	/**
	 * This method gets an instance of {@link Transaction} based on the
	 * transaction reference.
	 * 
	 * @param transactionRef
	 *            transaction reference
	 * @return an instance of {@link Transaction}
	 * 
	 * @throws NullPointerException
	 *             if transactionRef is null
	 */
	@Override
	public Transaction getTransaction(String transactionRef) {
		// validations
		Validate.notNull(transactionRef, "Please provide a valid transaction reference.");
		// get the transaction based on transaction reference
		AccountTransaction transaction = transactionRepository.findByTransactionRef(transactionRef);
		return transaction.toTransactionValueObject();
	}

}
