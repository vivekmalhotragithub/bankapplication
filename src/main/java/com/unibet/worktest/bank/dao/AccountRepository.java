package com.unibet.worktest.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.entity.Account;

/**
 *
 *
 */
@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Modifying
	@Transactional
	@Override
	public Account save(Account account);

	/**
	 * find a Unique Account by an Account Reference
	 * 
	 * @return
	 */
	public Account findByAccountRef(String accountRef);

}
