package com.mvnproj.urlshortening.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.mvnproj.urlshortening.request.LoginRequest;
import com.mvnproj.urlshortening.request.TinyUrlRequest;
import com.mvnproj.urlshortening.request.UserDataRequest;
import com.mvnproj.urlshortening.response.ResponseData;
import com.mvnproj.urlshortening.service.TinyUrlService;
import com.mvnproj.urlshortening.service.UserService;

@RestController
@RequestMapping("/api")
public class UrlShorteningController {

	@Autowired
	private UserService userService;

	@Autowired
	private TinyUrlService tinyUrlService;

	@JsonView(ResponseData.CreateView.class)
	@PostMapping("/user/create-account")
	public ResponseEntity<ResponseData> createAccount(@RequestBody @Valid UserDataRequest userDataRequest) {

		return userService.createAccount(userDataRequest);
	}

	@JsonView(ResponseData.AuthenticateView.class)
	@PostMapping("/user/login")
	public ResponseEntity<ResponseData> generateToken(@RequestBody LoginRequest loginRequest) throws Exception {

		return userService.generateToken(loginRequest);

	}

	@JsonView(ResponseData.ShortenUrlView.class)
	@PostMapping("/user/shorten-url")
	public ResponseEntity<ResponseData> shortenUrl(@RequestHeader String authToken,
			@RequestBody TinyUrlRequest tinyUrlRequest) {

		return tinyUrlService.shortenUrl(authToken, tinyUrlRequest);
	}

}
