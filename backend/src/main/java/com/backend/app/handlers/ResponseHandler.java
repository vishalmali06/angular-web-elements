package com.backend.app.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	public static ResponseEntity<Object> generateResponse(Boolean response, String message, HttpStatus status,
			Object responseObj) {
		Map<String, Object> map = new HashMap<>();

		map.put("data", responseObj);
		map.put("success", response);
		map.put("message", message);
		map.put("status", status.value());

		return new ResponseEntity<>(map, status);
	}
}
