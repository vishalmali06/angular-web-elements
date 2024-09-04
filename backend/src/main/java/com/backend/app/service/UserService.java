package com.backend.app.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.app.dto.UserDto;
import com.backend.app.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {

	public List<User> getUsers();

	public Page<User> getUsersBySortingAndPagination(int offset, int pageSize, String sortBy, String sort);

	public User getUserById(long userId);

	public User saveUser(User user);

	public UserDto updateUser(String jData) throws JsonProcessingException;

	public User deleteUserById(long userId);
}
