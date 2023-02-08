package com.mvnproj.urlshortening.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mvnproj.urlshortening.request.TinyUrlRequest;
import com.mvnproj.urlshortening.response.ResponseData;

@Service
public interface TinyUrlService {

	ResponseEntity<ResponseData> shortenUrl(String authToken, TinyUrlRequest tinyUrlRequest);

}
