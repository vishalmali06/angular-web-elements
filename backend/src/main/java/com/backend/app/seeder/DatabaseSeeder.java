package com.backend.app.seeder;

import java.util.Date;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.backend.app.model.Account;
import com.backend.app.model.User;
import com.backend.app.repository.UserRepository;

@Component
public class DatabaseSeeder {
	private UserRepository userRepository;

	public DatabaseSeeder(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		User user;
		Account account;
		Account secondAccount;
		if (userRepository.findAll().isEmpty()) {
			for (int i = 1; i <= 200; i++) {
				user = new User();
				user.setDob(new Date());
				user.setEmailId("a@mail.com");
				user.setGender(User.Gender.M);
				user.setMobileNo("1234567890");
				user.setSecondaryMobileNo("0");
				user.setName("user" + i + "");

				account = new Account();
				account.setAccountBalance(1000);
				account.setAccountType(Account.AccountType.C);
				account.setBranchName("abc");
				account.setUser(user);

				secondAccount = new Account();
				secondAccount.setAccountBalance(1000);
				secondAccount.setAccountType(Account.AccountType.C);
				secondAccount.setBranchName("xyz");
				secondAccount.setUser(user);

				user.addAccount(account);
				user.addAccount(secondAccount);

				userRepository.save(user);
			}

		}
	}
}
