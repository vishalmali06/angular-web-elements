package com.backend.app.deserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.backend.app.model.Account;
import com.backend.app.model.User;
import com.backend.app.model.Account.AccountType;
import com.backend.app.model.User.Gender;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDeserializer extends StdDeserializer<User> {

	private static final long serialVersionUID = 1L;

	public UserDeserializer() {
		this(null);
	}

	public UserDeserializer(Class<User> vc) {
		super(vc);
	}

	@Override
	public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		log.info("deserialize: " + jp.toString());
		JsonNode node = jp.getCodec().readTree(jp);
		long userId = node.get("userId").asLong();
		String name = node.get("name").asText();
		String emailId = node.get("emailId").asText();
		String mobileNo = node.get("mobileNo").asText();
		String secondaryMobileNo = node.get("secondaryMobileNo").asText();
		String gender = node.get("gender").asText();
		String dobStr = node.get("dob").asText();

		Date dob = new Date();
		try {
			dob = new SimpleDateFormat("mm/dd/yyyy").parse(dobStr);
		} catch (ParseException e) {
			dob = new Date();
		}

		User user = new User();
		user.setUserId(userId);
		user.setName(name);
		user.setEmailId(emailId);
		user.setMobileNo(mobileNo);
		user.setDob(dob);
		user.setSecondaryMobileNo(secondaryMobileNo);
		if (gender.equals("M"))
			user.setGender(Gender.M);
		else
			user.setGender(Gender.F);

		if (node.has("accounts")) {
			ArrayNode accounts = (ArrayNode) node.get("accounts");
			if (accounts.isArray()) {
				for (JsonNode account : accounts) {
					long accountId = account.get("accountId").asLong();
					String branchName = account.get("branchName").asText();
					String accountType = account.get("accountType").asText();
					double accountBalance = account.get("accountBalance").asDouble();

					Account acc = new Account();
					acc.setAccountId(accountId);
					acc.setBranchName(branchName);
					if (accountType.equals("S"))
						acc.setAccountType(AccountType.S);
					else
						acc.setAccountType(AccountType.C);
					acc.setAccountBalance(accountBalance);
					acc.setUser(user);

					user.addAccount(acc);
				}
			}
		}

		return user;
	}

}
