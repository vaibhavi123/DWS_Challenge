package com.db.awmd.challenge.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.exception.TransferFundException;
import com.db.awmd.challenge.service.TransferService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class TransferController {

	private final TransferService transactionService;

	@Autowired
	public TransferController(TransferService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * @param accountFromId id of withdrawn account
	 * @param accountToId id of credit account
	 * @param amount transfer amount
	 * @return
	 */
	@PostMapping(path = "/transfer")
	public ResponseEntity<Object> transferMoney(
			@RequestParam(value = "accountFrom", required = true) String accountFromId,
			@RequestParam(value = "accountTo", required = true) String accountToId,
			@RequestParam(value = "amount", required = true) BigDecimal amount) {
		try {
			this.transactionService.transfer(accountFromId, accountToId, amount);
		} catch (TransferFundException ex) {
			log.debug("Error while executing transfer:{}",ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
