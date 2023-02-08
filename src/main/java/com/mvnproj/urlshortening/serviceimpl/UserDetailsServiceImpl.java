package com.mvnproj.urlshortening.serviceimpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mvnproj.urlshortening.entity.UserData;
import com.mvnproj.urlshortening.repository.UserDataRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private  UserDataRepository userDataRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserData user = userDataRepository.findByUserName(username);
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
	}
}
