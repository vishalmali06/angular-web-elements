package com.backend.app.deserializer;

import java.io.IOException;

import com.backend.app.model.Account;
import com.backend.app.model.Account.AccountType;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountDeserializer extends StdDeserializer<Account> {

	private static final long serialVersionUID = 1L;

	public AccountDeserializer() {
		this(null);
	}

	public AccountDeserializer(Class<Account> vc) {
		super(vc);

	}

	@Override
	public Account deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		log.info("Account Deserializer");
		JsonNode node = jp.getCodec().readTree(jp);
		log.info(node.toString());
		long accountId = node.get("accountId").asLong();
		String branchName = node.get("branchName").asText();
		String accountType = node.get("accountType").asText();
		double accountBalance = node.get("accountBalance").asDouble();

		Account account = new Account();
		account.setAccountId(accountId);
		account.setBranchName(branchName);
		if (accountType.equals("S"))
			account.setAccountType(AccountType.S);
		else
			account.setAccountType(AccountType.C);
		account.setAccountBalance(accountBalance);

		return account;
	}

}
