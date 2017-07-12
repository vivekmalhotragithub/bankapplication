package com.unibet.worktest.bank.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.entity.AccountTransaction;

/**
 * 
 * @author vivekmalhotra
 *
 */
@Transactional(readOnly = true)
@Repository
public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {

	@Modifying
	@Transactional
	public AccountTransaction save(AccountTransaction transaction);

	@Query("select distinct trans from AccountTransaction trans INNER JOIN FETCH trans.transactionLegs legs INNER JOIN FETCH legs.account acc where acc.accountRef = :accountRef")
	public List<AccountTransaction> findByAccountRef(@Param("accountRef") String accountRef);

	public AccountTransaction findByTransactionRef(String transactionRef);

}
