package com.backend.app.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.app.model.User;

public interface UserDAO {

	public Iterable<User> getUsers();

	public Page<User> getUsersBySortingAndPagination(Pageable pageable);

	public Optional<User> getUserById(long userId);

	public User saveUser(User user);

	public User updateUser(User user);

	public boolean deleteUserById(long userId);

}
