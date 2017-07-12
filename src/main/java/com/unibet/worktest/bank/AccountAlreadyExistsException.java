package com.unibet.worktest.bank;

/**
 * Business exception thrown if a referenced account already exist.
 *
 * @author vivekmalhotra
 */
public class AccountAlreadyExistsException extends BusinessException {
    public AccountAlreadyExistsException(String accountRef) {
        super("Account already exists with reference '" + accountRef + "'");
    }
}
