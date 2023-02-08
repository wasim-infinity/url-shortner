package com.mvnproj.urlshortening.exception;

import lombok.Data;

@Data
public class ExceptionDetails {

	private int statusCode;
	private String statusMessage;
}
