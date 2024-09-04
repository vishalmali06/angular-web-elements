package com.backend.app.dao;

import java.util.Optional;

import com.backend.app.model.Account;
import com.backend.app.model.User;

public interface AccountDAO {

	public Iterable<Account> getAccounts();

	public Optional<Account> getAccountById(long accountId);

	public Account saveAccount(Account account);

	public Account updateAccount(Account account);

	public boolean deleteAccountById(long accountId);

	public Iterable<Account> getAccountsByUser(User user);
}
