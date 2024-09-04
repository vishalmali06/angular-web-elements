package com.backend.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.UserDto;
import com.backend.app.handlers.ResponseHandler;
import com.backend.app.model.User;
import com.backend.app.service.UserService;
import com.backend.app.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class UserController {

	private UserService userService;

	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@GetMapping("/user")
	public ResponseEntity<Object> getUsers() throws Exception {
		List<User> users = userService.getUsers();
		List<UserDto> usersdto = new ArrayList<>();
		users.forEach(user -> usersdto.add(user.convertToDto()));
		log.info("Users: " + users.toString());
		if (users.isEmpty()) {
			log.error("No User Found");
			return ResponseHandler.generateResponse(false, "No User Found.", HttpStatus.NOT_FOUND, "");
		}
		log.info("User Found: " + users.toString());
		return ResponseHandler.generateResponse(true, "Found Users", HttpStatus.OK, usersdto);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<Object> getUserById(@PathVariable("userId") String userId) {
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
			e.printStackTrace();
			log.error("Failed to parse user ID: " + userId);
			return ResponseHandler.generateResponse(false, "Invalid Input " + userId + ".",
					HttpStatus.UNPROCESSABLE_ENTITY, "");
		}

		User user = userService.getUserById(uid);
		if (user == null) {
			log.error("Invalid user ID: " + uid);
			return ResponseHandler.generateResponse(false, "User Not Found With ID " + uid + ".", HttpStatus.NOT_FOUND,
					"");
		}
		log.info("Get User: " + user.toString());
		return ResponseHandler.generateResponse(true, "Found User", HttpStatus.OK, user.convertToDto());
	}

	@PostMapping("/user")
	@Transactional
	public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
		try {
			log.info(user.getAccounts().toString());
			User u = userService.saveUser(user);

			if (u != null) {
				log.info("Saved User: " + user.toString());
				return ResponseHandler.generateResponse(true, "Saved User Data", HttpStatus.CREATED, u.convertToDto());
			} else {
				log.error("Failed to save acount");
				return ResponseHandler.generateResponse(false, "Failed To Save User Data",
						HttpStatus.INTERNAL_SERVER_ERROR, "");
			}
		} catch (Exception e) {
			log.error("Exception: Failed to save acount");
			e.printStackTrace();
			return ResponseHandler.generateResponse(false, "Failed To Save User Data", HttpStatus.INTERNAL_SERVER_ERROR,
					"");
		}

	}

	@PutMapping("/user")
	@Transactional
	public ResponseEntity<Object> updateUser(@RequestBody String jData) throws JsonProcessingException {
		try {
			UserDto updatedUser = userService.updateUser(jData);
			if (updatedUser != null) {
				log.info("Updated User: " + updatedUser.toString());
				return ResponseHandler.generateResponse(true, "Updated User Data", HttpStatus.OK, updatedUser);
			} else {
				log.error("Invalid user ID");
				return ResponseHandler.generateResponse(false, "User Does Not Exists", HttpStatus.NOT_FOUND, "");
			}

		} catch (Exception e) {
			log.error("Exception: Failed to update acount");
			e.printStackTrace();
			return ResponseHandler.generateResponse(false, "Failed To Update User Data",
					HttpStatus.INTERNAL_SERVER_ERROR, "");
		}
	}

	@DeleteMapping("/user/{userId}")
	@Transactional
	public ResponseEntity<Object> deleteUserById(@PathVariable("userId") String userId) {
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
			e.printStackTrace();
			return ResponseHandler.generateResponse(false, "Invalid Input " + userId + ".",
					HttpStatus.UNPROCESSABLE_ENTITY, "");
		}

		try {
			User acc = userService.deleteUserById(uid);
			if (acc != null) {
				log.info("Deleted User: " + acc.toString());
				return ResponseHandler.generateResponse(true, "Deleted User Data Successfully", HttpStatus.OK, "");

			} else {
				log.error("Invalid user ID: " + uid);
				return ResponseHandler.generateResponse(false, "User Does Not Exist with ID " + uid,
						HttpStatus.NOT_FOUND, "");
			}
		} catch (Exception e) {
			log.error("Exception: Failed to delete acount");
			e.printStackTrace();
			return ResponseHandler.generateResponse(false, "Failed To Delete User Data",
					HttpStatus.INTERNAL_SERVER_ERROR, "");
		}
	}

	@GetMapping("/user/pageable")
	public ResponseEntity<Object> getUsersByPagination(@RequestParam("offset") int offset,
			@RequestParam("pageSize") int pageSize, @RequestParam("sortBy") String sortBy,
			@RequestParam("sort") String sort) {

		Page<User> users = userService.getUsersBySortingAndPagination(offset, pageSize, sortBy, sort);
		List<UserDto> usersdto = new ArrayList<>();
		users.forEach(user -> usersdto.add(user.convertToDto()));
		log.info("Users: " + users.toString());
		if (users.isEmpty()) {
			log.error("No User Found");
			return ResponseHandler.generateResponse(false, "No User Found.", HttpStatus.NOT_FOUND, "");
		}
		log.info("User Found: " + users.toString());

		return ResponseHandler.generateResponse(true, "Found Users", HttpStatus.OK, users);
	}

}
