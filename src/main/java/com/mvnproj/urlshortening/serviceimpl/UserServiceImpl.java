package com.mvnproj.urlshortening.serviceimpl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mvnproj.urlshortening.authentication.JwtUtil;
import com.mvnproj.urlshortening.entity.UserData;
import com.mvnproj.urlshortening.repository.UserDataRepository;
import com.mvnproj.urlshortening.request.LoginRequest;
import com.mvnproj.urlshortening.request.UserDataRequest;
import com.mvnproj.urlshortening.response.ResponseData;
import com.mvnproj.urlshortening.response.ResponseHelper;
import com.mvnproj.urlshortening.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDataRepository userDataRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	@Override
	public ResponseEntity<ResponseData> createAccount(UserDataRequest userDataRequest) {

		UserData userData = userDataRepository.findByUserName(userDataRequest.getUsername());
		ResponseData response = null;
		if (Objects.isNull(userData)) {
			UserData newUser = new UserData();
			newUser.setUserName(userDataRequest.getUsername());
			newUser.setPhone(userDataRequest.getPhone());
			newUser.setEmail(userDataRequest.getEmail());
			newUser.setPassword(pwdEncoder.encode(userDataRequest.getPassword()));
			newUser.setCreatedAt(LocalDateTime.now());
			userData = userDataRepository.save(newUser);
		} else {
			response = ResponseHelper.responseSender(
					"user with username : " + userDataRequest.getUsername() + " already exists !!!.",
					HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ResponseData>(response, HttpStatus.BAD_REQUEST);
		}

		if (userData.getId() > 0) {
			response = ResponseHelper.responseSender("Successfully account created !!!.", HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseData>(response, HttpStatus.CREATED);
		} else {
			response = ResponseHelper.responseSender("Unable to create account !!!. Please try again.",
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ResponseData>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<ResponseData> generateToken(LoginRequest loginRequest) {

		ResponseData response = null;
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		} catch (Exception ex) {
			response = ResponseHelper.responseSender("inavalid username/password", HttpStatus.UNAUTHORIZED.value());
			return new ResponseEntity<ResponseData>(response, HttpStatus.UNAUTHORIZED);
		}
		response = ResponseHelper.responseSender("Success", HttpStatus.OK.value(),
				jwtUtil.generateToken(loginRequest.getUserName()), loginRequest.getUserName());
		return new ResponseEntity<ResponseData>(response, HttpStatus.OK);

	}

}
