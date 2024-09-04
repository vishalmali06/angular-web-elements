package com.backend.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.AccountDto;
import com.backend.app.handlers.ResponseHandler;
import com.backend.app.model.Account;
import com.backend.app.model.User;
import com.backend.app.service.AccountService;
import com.backend.app.service.UserService;
import com.backend.app.service.impl.AccountServiceImpl;
import com.backend.app.service.impl.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class AccountController {

	private AccountService accountService;
	private UserService userService;

	public AccountController(AccountServiceImpl accountService, UserServiceImpl userService) {
		this.accountService = accountService;
		this.userService = userService;
	}

	@GetMapping("/account")
	public ResponseEntity<Object> getAccounts() {
		List<AccountDto> accounts = accountService.getAccounts();
		log.info("Accounts: " + accounts.toString());
		if (accounts.isEmpty()) {
			log.error("No User Found");
			return ResponseHandler.generateResponse(false, "No User Found.", HttpStatus.NOT_FOUND, "");
		}
		log.info("User Found: " + accounts.toString());
		return ResponseHandler.generateResponse(true, "Found Users", HttpStatus.OK, accounts);
	}

	@GetMapping("/account/user/{userId}")
	public ResponseEntity<Object> getAccountByUser(@PathVariable String userId) {

		long uid = 0l;
		try {
			if (userId.length() != 5) {
				log.error("Invalid user length: " + userId);
				return ResponseHandler.generateResponse(false, "User ID must be 10 digits long " + userId + ".",
						HttpStatus.UNPROCESSABLE_ENTITY, "");
			} else {
				uid = Long.parseLong(userId);
				log.info("Parsed User ID: " + uid);
			}
		} catch (NumberFormatException e) {
			log.error("Failed to parse user ID: " + userId);
			return ResponseHandler.generateResponse(false, "Invalid Input :" + userId + ".",
					HttpStatus.UNPROCESSABLE_ENTITY, "");
		}

		User user = userService.getUserById(uid);
		if (user == null) {
			log.error("Invalid user ID: " + uid);
			return ResponseHandler.generateResponse(false, "User Not Found With ID " + uid + ".", HttpStatus.NOT_FOUND,
					"");
		} else {
			List<AccountDto> accounts = accountService.getAccountsByUser(user);
			log.info("Accounts: " + accounts.toString());
			if (accounts.isEmpty()) {
				log.error("No User Found");
				return ResponseHandler.generateResponse(false, "No User Found.", HttpStatus.NOT_FOUND, "");
			}
			log.info("User Found: " + accounts.toString());
			return ResponseHandler.generateResponse(true, "Found Users", HttpStatus.OK, accounts);

		}
	}

	@GetMapping("/account/{accountId}")
	public ResponseEntity<Object> getAccountById(@PathVariable("accountId") String accountId) {
		long uid = 0l;
		try {
			if (accountId.length() != 10) {
				log.error("Invalid account length: " + accountId);
				return ResponseHandler.generateResponse(false, "Account ID must be 10 digits long " + accountId + ".",
						HttpStatus.UNPROCESSABLE_ENTITY, "");
			} else {
				uid = Long.parseLong(accountId);
				log.info("Parsed Account ID: " + uid);
			}
		} catch (NumberFormatException e) {
			log.error("Failed to parse account ID: " + accountId);
			return ResponseHandler.generateResponse(false, "Invalid Input " + accountId + ".",
					HttpStatus.UNPROCESSABLE_ENTITY, "");
		}

		Account account = accountService.getAccountById(uid);
		if (account == null) {
			log.error("Invalid account ID: " + uid);
			return ResponseHandler.generateResponse(false, "Account Not Found With ID " + uid + ".",
					HttpStatus.NOT_FOUND, "");
		}
		log.info("Get Account: " + account.toString());
		return ResponseHandler.generateResponse(true, "Found Account", HttpStatus.OK, account.convertToDto());
	}

	@PostMapping("/account")
	@Transactional
	public ResponseEntity<Object> saveAccount(@Valid @RequestBody Account account) {
		try {
			AccountDto acc = null;
			acc = accountService.saveAccount(account);
			if (acc != null) {
				log.info("Saved Account: " + account.toString());
				return ResponseHandler.generateResponse(true, "Saved Account Data", HttpStatus.CREATED, acc);
			} else {
				log.error("Failed to save acount");
				return ResponseHandler.generateResponse(false, "Failed To Save Account Data",
						HttpStatus.INTERNAL_SERVER_ERROR, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception: Failed to save acount");
			return ResponseHandler.generateResponse(false, "Failed To Save Account Data",
					HttpStatus.INTERNAL_SERVER_ERROR, "");
		}

	}

	@PutMapping("/account")
	@Transactional
	public ResponseEntity<Object> updateAccount(@Valid @RequestBody Account account) {
		try {
			AccountDto accountDto = accountService.updateAccount(account);
			if (accountDto != null) {
				log.info("Updated Account: " + account.toString());
				return ResponseHandler.generateResponse(true, "Updated Account Data", HttpStatus.OK, accountDto);
			} else {
				log.error("Invalid account ID");
				return ResponseHandler.generateResponse(false,
						"Account Does Not Exist with ID " + account.getAccountId(), HttpStatus.NOT_FOUND, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception: Failed to update acount");
			return ResponseHandler.generateResponse(false, "Failed To Update Account Data",
					HttpStatus.INTERNAL_SERVER_ERROR, "");
		}
	}

	@DeleteMapping("/account/{accountId}")
	@Transactional
	public ResponseEntity<Object> deleteAccountById(@PathVariable("accountId") String accountId) {
		long id = 0l;
		try {
			if (accountId.length() != 10) {
				log.error("Invalid account length: " + accountId);
				return ResponseHandler.generateResponse(false, "Account ID must be 10 digits long " + accountId + ".",
						HttpStatus.UNPROCESSABLE_ENTITY, "");
			} else {
				id = Long.parseLong(accountId);
				log.info("Parsed Account ID: " + id);
			}
		} catch (NumberFormatException e) {
			log.error("Failed to parse account ID: " + accountId);
			return ResponseHandler.generateResponse(false, "Invalid Input " + accountId + ".",
					HttpStatus.UNPROCESSABLE_ENTITY, "");
		}

		try {
			AccountDto accountDto = accountService.deleteAccountById(id);
			if (accountDto != null) {
				log.info("Deleted Account: " + accountDto.toString());
				return ResponseHandler.generateResponse(true, "Deleted Account Data Successfully", HttpStatus.OK, "");

			} else {
				log.error("Invalid account ID: " + id);
				return ResponseHandler.generateResponse(true, "Account Does Not Exist with ID " + id,
						HttpStatus.NOT_FOUND, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception: Failed to delete acount");
			return ResponseHandler.generateResponse(false, "Failed To Delete Account Data",
					HttpStatus.INTERNAL_SERVER_ERROR, "");
		}
	}

}
