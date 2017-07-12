package com.unibet.worktest.bank;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.unibet.worktest.bank.application.BankApplication;

import static junit.framework.Assert.assertEquals;

/**
 * Functional test that demonstrate the key functionality of the
 * banking service and verifies that the solution works.
 * <p/>
 * Please ensure that this test pass without any failures before
 * submitting your solution.
 *
 * @author Unibet
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sealed
public final class BankFunctionalTest {
    // NOTE: Account names are opaque (no built-in semantics)

    private static final String CASH_ACCOUNT_1 = "cash:1:EUR";

    private static final String CASH_ACCOUNT_2 = "cash:2:SEK";

    private static final String DEPOSIT_ACCOUNT_1 = "deposit:1:EUR";

    private static final String DEPOSIT_ACCOUNT_2 = "deposit:2:SEK";

    private AccountService accountService;

    private TransferService transferService;

    @Before
    public void setupInitialDataBeforeEachTest() throws Exception {
        BankFactory bankFactory = (BankFactory) Class.forName(
                "com.unibet.worktest.bank.BankFactoryImpl").newInstance();

        accountService = bankFactory.getAccountService();
        transferService = bankFactory.getTransferService();

        bankFactory.setupInitialData();
    }

    @Test
    public void accountBalancesUpdatedAfterTransfer() {
        accountService.createAccount(CASH_ACCOUNT_1, Money.euros("1000.00"));
        accountService.createAccount(DEPOSIT_ACCOUNT_1, Money.euros("0.00"));

        assertEquals(Money.euros("1000.00"), accountService.getAccountBalance(CASH_ACCOUNT_1));
        assertEquals(Money.euros("0.00"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_1));

        transferService.transferFunds(TransferRequest.builder()
                .reference("tx1")
                .account(CASH_ACCOUNT_1).amount(Money.euros("-5.00"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("5.00"))
                .build());

        transferService.transferFunds(TransferRequest.builder()
                .reference("tx2")
                .account(CASH_ACCOUNT_1).amount(Money.euros("-10.50"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("10.50"))
                .build());

        assertEquals(Money.euros("984.50"), accountService.getAccountBalance(CASH_ACCOUNT_1));
        assertEquals(Money.euros("15.50"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_1));

        List<Transaction> t1 = transferService.findTransactions(CASH_ACCOUNT_1);
        assertEquals(2, t1.size());

        List<Transaction> t2 = transferService.findTransactions(DEPOSIT_ACCOUNT_1);
        assertEquals(2, t2.size());
    }

    @Test
    public void accountBalancesUpdatedAfterMultiLegTransfer() {
        accountService.createAccount(CASH_ACCOUNT_1, Money.euros("1000.00"));
        accountService.createAccount(DEPOSIT_ACCOUNT_1, Money.euros("0.00"));

        assertEquals(Money.euros("1000.00"), accountService.getAccountBalance(CASH_ACCOUNT_1));
        assertEquals(Money.euros("0.00"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_1));

        transferService.transferFunds(TransferRequest.builder()
                .reference("tx1")
                .account(CASH_ACCOUNT_1).amount(Money.euros("-5.00"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("5.00"))
                .account(CASH_ACCOUNT_1).amount(Money.euros("-10.50"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("10.50"))
                .account(CASH_ACCOUNT_1).amount(Money.euros("-2.00"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("1.00"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("1.00"))
                .build());

        assertEquals(Money.euros("982.50"), accountService.getAccountBalance(CASH_ACCOUNT_1));
        assertEquals(Money.euros("17.50"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_1));
    }

    @Test
    public void accountBalancesUpdatedAfterMultiLegMultiCurrencyTransfer() {
        accountService.createAccount(CASH_ACCOUNT_1, Money.euros("1000.00"));
        accountService.createAccount(DEPOSIT_ACCOUNT_1, Money.euros("0.00"));
        accountService.createAccount(CASH_ACCOUNT_2, Money.kronor("1000.00"));
        accountService.createAccount(DEPOSIT_ACCOUNT_2, Money.kronor("0.00"));

        assertEquals(Money.kronor("1000.00"), accountService.getAccountBalance(CASH_ACCOUNT_2));
        assertEquals(Money.kronor("0.00"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_2));

        transferService.transferFunds(TransferRequest.builder()
                .reference("tx1")
                .account(CASH_ACCOUNT_1).amount(Money.euros("-5.00"))
                .account(DEPOSIT_ACCOUNT_1).amount(Money.euros("5.00"))
                .account(CASH_ACCOUNT_2).amount(Money.kronor("-10.50"))
                .account(DEPOSIT_ACCOUNT_2).amount(Money.kronor("10.50"))
                .build());

        assertEquals(Money.euros("995.00"), accountService.getAccountBalance(CASH_ACCOUNT_1));
        assertEquals(Money.euros("5.00"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_1));
        assertEquals(Money.kronor("989.50"), accountService.getAccountBalance(CASH_ACCOUNT_2));
        assertEquals(Money.kronor("10.50"), accountService.getAccountBalance(DEPOSIT_ACCOUNT_2));
    }
}
