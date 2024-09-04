package com.backend.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.app.model.Account;
import com.backend.app.model.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Iterable<Account> findByUser(User user);

}
