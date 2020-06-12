package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.TransferFundException;
import com.db.awmd.challenge.repository.TransferRepository;

import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

	@Getter
	public final TransferRepository transferRepository;

	@Autowired
	AccountsService accountServce;

	@Autowired
	public TransferService(TransferRepository transferRepository) {
		this.transferRepository = transferRepository;
	}

	/**
	 * @param accountFromId Account Id of from which transfer amount debited
	 * @param accountToId   Account Id of to which transfer amount credited
	 * @param amount        amount to be transfer
	 */
	public boolean transfer(String accountFromId, String accountToId, BigDecimal amount) throws TransferFundException {

		if ((amount.signum() == -1))
			throw new TransferFundException("Transfer Amount cannot be negative");

		Account from = accountServce.getAccount(accountFromId);
		Account to = accountServce.getAccount(accountToId);

		if (from == null || to == null) {
			throw new TransferFundException("Account does not exist");
		}

		if (amount.compareTo(from.getBalance()) == 1) {
			throw new TransferFundException("Withdraw amount is invalid");
		}

		Transfer transfer = new Transfer(from, to, amount);
		if (transferRepository.transfer(transfer)) {
			NotificationService notificationService = new EmailNotificationService();
			notificationService.notifyAboutTransfer(to, "UPDATE: Fund Deposited from " + from.getAccountId());
			notificationService.notifyAboutTransfer(from, "UPDATE: Fund Debited to " + to.getAccountId());
			return true;
		}
		return false;
	}
}
