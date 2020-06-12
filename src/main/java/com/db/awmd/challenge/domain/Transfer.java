package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class Transfer {

	@NotNull
	@NotEmpty
	Account from;

	@NotNull
	@NotEmpty
	Account to;

	@NotNull
	@Min(value = 1, message = "transfer amount must be greater than 1")
	BigDecimal transferAmount;

	public void withdrawFromAccount() {
		from.setBalance(from.getBalance().subtract(transferAmount));
	}

	public void depositToAccount() {
		to.setBalance(to.getBalance().add(transferAmount));
	}

	public Transfer(Account accountFrom, Account accountTo, BigDecimal amount) {
		this.from = accountFrom;
		this.to = accountTo;
		this.transferAmount = amount;
	}
}
