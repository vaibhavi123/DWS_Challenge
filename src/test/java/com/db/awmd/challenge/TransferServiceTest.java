package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.TransferFundException;
import com.db.awmd.challenge.repository.TransferRepository;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.TransferService;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferServiceTest {

	@Autowired
	public TransferService transactionService;

	@Mock
	public TransferRepository transferRepository;

	@Autowired
	public AccountsService accountsService;

	private Account accountTo;
	private Account accountFrom;
	private Transfer transfer;
	private String accountFromId = "5555";
	private String accoutnToId = "6666";

	@Before
	public void prepareAccountObjets() {
		accountsService.getAccountsRepository().clearAccounts();
		accountFrom = new Account(accountFromId, new BigDecimal("100"));
		accountTo = new Account(accoutnToId, new BigDecimal("200"));
		accountsService.createAccount(accountFrom);
		accountsService.createAccount(accountTo);

	}

	@Test
	public void transferMoney() {
		when(transferRepository.transfer(transfer)).thenReturn(true);
		Assert.assertEquals(transactionService.transfer(accountFromId, accoutnToId, new BigDecimal(50)), true);

	}

	@Test
	public void negativeAmountTest() {
		try {
			transactionService.transfer("5555", "6666", new BigDecimal(-50));
			fail("Should have failed when adding duplicate account");
		} catch (TransferFundException ex) {
			assertThat(ex.getMessage()).isEqualTo("Transfer Amount cannot be negative");
		}
	}

	@Test
	public void invalidAmountTest() {
		try {
			transactionService.transfer("5555", "6666", new BigDecimal(200));
			fail("Should have failed when adding duplicate account");
		} catch (TransferFundException ex) {
			assertThat(ex.getMessage()).isEqualTo("Withdraw amount is invalid");
		}

	}

	@Test
	public void zeroAmountTest() {
		try {
			transactionService.transfer("5555", "7777", new BigDecimal(50));
			fail("Should have failed when adding duplicate account");
		} catch (TransferFundException ex) {
			assertThat(ex.getMessage()).isEqualTo("Account does not exist");
		}
	}
}
