package com.backend.app.dao.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.backend.app.dao.UserDAO;
import com.backend.app.model.User;
import com.backend.app.repository.UserRepository;

@Component
@Transactional
public class UserDAOImpl implements UserDAO {

	private UserRepository userRepository;

	public UserDAOImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public Page<User> getUsersBySortingAndPagination(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public Optional<User> getUserById(long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean deleteUserById(long userId) {
		try {
			userRepository.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
