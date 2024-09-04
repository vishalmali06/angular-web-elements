package com.backend.app.service;

import java.util.List;

import com.backend.app.dto.AccountDto;
import com.backend.app.model.Account;
import com.backend.app.model.User;

public interface AccountService {

	public List<AccountDto> getAccounts();

	public Account getAccountById(long accountId);

	public AccountDto saveAccount(Account account);

	public AccountDto updateAccount(Account account);

	public AccountDto deleteAccountById(long accountId);

	public List<AccountDto> getAccountsByUser(User user);

}
