package com.backend.app.dao.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.backend.app.dao.AccountDAO;
import com.backend.app.model.Account;
import com.backend.app.model.User;
import com.backend.app.repository.AccountRepository;

@Component
@Transactional
public class AccountDAOImpl implements AccountDAO {

	private AccountRepository accountRepository;

	public AccountDAOImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Iterable<Account> getAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public Optional<Account> getAccountById(long accountId) {
		return accountRepository.findById(accountId);
	}

	@Override
	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public Account updateAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public boolean deleteAccountById(long accountId) {
		try {
			accountRepository.deleteById(accountId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Iterable<Account> getAccountsByUser(User user) {
		return accountRepository.findByUser(user);
	}

}
