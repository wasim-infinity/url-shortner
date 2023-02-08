package com.mvnproj.urlshortening.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class InvalidTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int statusCode;

	private HttpStatus httpStatus;

	public InvalidTokenException(String msg, int statusCode, HttpStatus httpStatus) {
		super(msg);
		this.statusCode = statusCode;
		this.httpStatus = httpStatus;
	}

}
