package com.backend.app.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.app.dao.UserDAO;
import com.backend.app.dao.impl.UserDAOImpl;
import com.backend.app.dto.UserDto;
import com.backend.app.model.Account;
import com.backend.app.model.User;
import com.backend.app.model.Account.AccountType;
import com.backend.app.model.User.Gender;
import com.backend.app.service.AccountService;
import com.backend.app.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
	private UserDAO userDAO;
	private AccountService accountService;

	public UserServiceImpl(UserDAOImpl userDAO, AccountService accountService) {
		this.userDAO = userDAO;
		this.accountService = accountService;
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();
		userDAO.getUsers().forEach(users::add);
		return users;
	}

	@Override
	public Page<User> getUsersBySortingAndPagination(int offset, int pageSize, String sortBy, String sort) {
		Pageable pageable;
		if (sort.equals("asc"))
			pageable = PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.ASC, sortBy));

		else
			pageable = PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC, sortBy));

		return userDAO.getUsersBySortingAndPagination(pageable);
	}

	@Override
	public User getUserById(long userId) {
		Optional<User> user = userDAO.getUserById(userId);
		if (user.isPresent()) {
			return user.get();
		} else {
			log.info("User Not Found");
			return null;
		}
	}

	@Override
	public User saveUser(User user) {
		return userDAO.saveUser(user);
	}

	public User modifyUser(User updatedUser) {
		User user = this.getUserById(updatedUser.getUserId());
		if (user == null) {
			return null;
		} else {
			user.setName(updatedUser.getName());
			user.setEmailId(updatedUser.getEmailId());
			user.setMobileNo(updatedUser.getMobileNo());
			user.setSecondaryMobileNo(updatedUser.getSecondaryMobileNo());
			user.setGender(updatedUser.getGender());
			user.setDob(updatedUser.getDob());
			return userDAO.updateUser(user);
		}
	}

	@Override
	public User deleteUserById(long userId) {
		User user = this.getUserById(userId);
		if (user == null) {
			log.info("User Is Null");
			return null;
		} else {
			if (userDAO.deleteUserById(userId))
				return user;
			else
				return null;
		}

	}

	@Override
	public UserDto updateUser(String jData) throws JsonProcessingException {
		try {
			JsonNode responseJson = new ObjectMapper().readTree(jData);
			log.info(responseJson.toString());
			JsonNode node = responseJson.get("user");

			User convertedUser = this.toUser(node);

			User user = saveUser(convertedUser);

			JsonNode jsonData = responseJson.get("jsonData");
			ArrayNode createdAccounts = (ArrayNode) jsonData.get("created");
			ArrayNode modifiedAccounts = (ArrayNode) jsonData.get("modified");
			ArrayNode deletedAccounts = (ArrayNode) jsonData.get("purged");

			log.info("created" + createdAccounts.toString());
			List<Account> addedAccounts = new ArrayList<>();
			if (createdAccounts.isArray()) {
				for (JsonNode account : createdAccounts) {
					Account newAccount = this.toAccount(account);
					newAccount.setUser(user);
					user.getAccounts().add(newAccount);
					addedAccounts.add(newAccount);
				}
			}

			log.info("Modified: " + modifiedAccounts.toString());
			List<Account> modifyAccounts = new ArrayList<>();
			if (modifiedAccounts.isArray()) {
				for (JsonNode account : modifiedAccounts) {
					Account newAccount = this.toAccount(account);
					Account oldAccount = accountService.getAccountById(newAccount.getAccountId());

					if (!isAccountModified(oldAccount, newAccount)) {
						user.getAccounts().remove(oldAccount);
						newAccount.setUser(user);
						user.getAccounts().add(newAccount);
						modifyAccounts.add(newAccount);
					}
				}
			}

			log.info("Deleted: " + deletedAccounts.toString());
			if (deletedAccounts.isArray()) {
				for (JsonNode account : deletedAccounts) {
					Account oldAccount = this.toAccount(account);
					user.getAccounts().remove(oldAccount);
				}

			}

			log.info("Updated User: " + user.toString());
			User updatedUser = this.saveUser(user);

			return updatedUser.convertToDto();

		} catch (Exception e) {
			log.error("Exception: Failed to update acount");
			e.printStackTrace();
			return null;
		}
	}

	private User toUser(JsonNode node) {

		long userId = node.get("userId").asLong();
		String name = node.get("name").asText();
		String emailId = node.get("emailId").asText();
		String mobileNo = node.get("mobileNo").asText();
		String secondaryMobileNo = node.get("secondaryMobileNo").asText();
		String gender = node.get("gender").asText();
		String dobStr = node.get("dob").asText();

		Date dob = new Date();
		try {
			dob = new SimpleDateFormat("dd-MMM-yyyy").parse(dobStr);
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

		return user;
	}

	private Account toAccount(JsonNode node) {
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

	private boolean isAccountModified(Account oldAccount, Account newAccount) {

		return oldAccount.getAccountId() == newAccount.getAccountId()
				&& oldAccount.getBranchName().equals(newAccount.getBranchName())
				&& oldAccount.getAccountType().equals(newAccount.getAccountType())
				&& oldAccount.getAccountBalance() == newAccount.getAccountBalance();
	}

}
