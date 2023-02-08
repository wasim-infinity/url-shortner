package com.mvnproj.urlshortening.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mvnproj.urlshortening.entity.UserData;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long>{

	public UserData findByUserName(String name);

	public UserData findByUserNameAndPassword(String userName, String password);
	
}
