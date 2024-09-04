package com.backend.app.dto;

import com.backend.app.deserializer.AccountDeserializer;
import com.backend.app.model.Account;
import com.backend.app.model.Account.AccountType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = AccountDeserializer.class)
public class AccountDto {
	private long accountId;
	private String branchName;
	private AccountType accountType;
	private double accountBalance;

	public AccountDto(Account other) {
		this.accountId = other.getAccountId();
		this.branchName = other.getBranchName();
		this.accountType = other.getAccountType();
		this.accountBalance = other.getAccountBalance();
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

}
