package com.db.awmd.challenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TransferControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	public AccountsService accountService;

	private Account accountTo;
	private Account accountFrom;
	private String accountFromId = "5555";
	private String accountToId = "6666";

	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
		MockitoAnnotations.initMocks(this);
		accountService.getAccountsRepository().clearAccounts();
		accountFrom = new Account(accountFromId, new BigDecimal("100"));
		accountTo = new Account(accountToId, new BigDecimal("200"));
		accountService.createAccount(accountFrom);
		accountService.createAccount(accountTo);
	}

	@Test
	public void tranferFund() throws Exception {

		this.mockMvc.perform(post("/v1/accounts/transfer?accountFrom=5555&accountTo=6666&amount=50"))
				.andExpect(status().isOk());
	}

	@Test
	public void tranferNegativeFund() throws Exception {
		this.mockMvc.perform(post("/v1/accounts/transfer?accountFrom=5555&accountTo=6666&amount=-50"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void accountIsLawOnBalance() throws Exception {
		this.mockMvc.perform(post("/v1/accounts/transfer?accountFrom=5555&accountTo=6666&amount=200"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void accountDoesNotExist() throws Exception {
		this.mockMvc.perform(post("/v1/accounts/transfer?accountFrom=5555&accountTo=777&amount=0"))
				.andExpect(status().isBadRequest());
	}
}
