package com.mvnproj.urlshortening.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mvnproj.urlshortening.request.LoginRequest;
import com.mvnproj.urlshortening.request.UserDataRequest;
import com.mvnproj.urlshortening.response.ResponseData;

@Service
public interface UserService {

	ResponseEntity<ResponseData> createAccount(UserDataRequest userDataRequest);

	ResponseEntity<ResponseData> generateToken(LoginRequest loginRequest);


}
