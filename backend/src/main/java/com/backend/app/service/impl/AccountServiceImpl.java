package com.backend.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.app.controller.UserController;
import com.backend.app.dao.AccountDAO;
import com.backend.app.dao.impl.AccountDAOImpl;
import com.backend.app.dto.AccountDto;
import com.backend.app.model.Account;
import com.backend.app.model.Account.AccountType;
import com.backend.app.model.User;
import com.backend.app.repository.AccountRepository;
import com.backend.app.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	private AccountDAO accountDao;

	public AccountServiceImpl(AccountDAOImpl accountDAOImpl) {
		this.accountDao = accountDAOImpl;
	}

	@Override
	public List<AccountDto> getAccounts() {
		List<AccountDto> accounts = new ArrayList<>();
		accountDao.getAccounts().forEach(account -> accounts.add(account.convertToDto()));
		return accounts;
	}

	@Override
	public Account getAccountById(long accountId) {
		Optional<Account> account = accountDao.getAccountById(accountId);
		if (account.isPresent()) {
			return account.get();
		} else {
			return null;
		}
	}

	@Override
	public AccountDto saveAccount(Account account) {
		return accountDao.saveAccount(account).convertToDto();

	}

	@Override
	public AccountDto updateAccount(Account updatedAccount) {
		Account account = this.getAccountById(updatedAccount.getAccountId());
		if (account == null) {
			return null;
		} else {
			account.setBranchName(updatedAccount.getBranchName());
			account.setAccountType(updatedAccount.getAccountType());
			account.setAccountBalance(updatedAccount.getAccountBalance());

			return accountDao.updateAccount(account).convertToDto();
		}

	}

	@Override
	public AccountDto deleteAccountById(long accountId) {
		Account account = this.getAccountById(accountId);
		if (account == null) {
			return null;
		} else {
			if (accountDao.deleteAccountById(accountId)) {
				log.info("deleted Account: " + accountId);
				return account.convertToDto();
			} else
				return null;
		}

	}

	@Override
	public List<AccountDto> getAccountsByUser(User user) {
		List<AccountDto> accounts = new ArrayList<>();
		accountDao.getAccountsByUser(user).forEach(account -> accounts.add(account.convertToDto()));
		return accounts;
	}

}