package com.mvnproj.urlshortening.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mvnproj.urlshortening.entity.ApiLog;
import com.mvnproj.urlshortening.entity.UserData;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long>{

	
	
}
