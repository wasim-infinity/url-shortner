package com.mvnproj.urlshortening.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> methodArgNotValidException(MethodArgumentNotValidException ex) {

		Map<String, String> errorResponse = new HashMap<String, String>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorResponse.put(error.getField(), error.getDefaultMessage());
		});

		return errorResponse;

	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ExceptionDetails> invalidToken(InvalidTokenException ex) {
		ExceptionDetails errorResponse = new ExceptionDetails();
		errorResponse.setStatusCode(ex.getStatusCode());
		errorResponse.setStatusMessage(ex.getMessage());
		return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
	}

}
