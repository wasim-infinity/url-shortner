package com.mvnproj.urlshortening.serviceimpl;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mvnproj.urlshortening.authentication.JwtUtil;
import com.mvnproj.urlshortening.entity.ApiLog;
import com.mvnproj.urlshortening.entity.UserData;
import com.mvnproj.urlshortening.repository.ApiLogRepository;
import com.mvnproj.urlshortening.repository.UserDataRepository;
import com.mvnproj.urlshortening.request.TinyUrlRequest;
import com.mvnproj.urlshortening.response.ResponseData;
import com.mvnproj.urlshortening.response.ResponseHelper;
import com.mvnproj.urlshortening.response.TinyUrlResponse;
import com.mvnproj.urlshortening.service.TinyUrlService;

@Service
@PropertySource("classpath:constant/constant.properties")
public class TinyUrlServiceImpl implements TinyUrlService {

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ApiLogRepository apiLogRepository;

	@Autowired
	private UserDataRepository userDataRepository;

	private String addAPIKeyInQueryParam(String url) {
		String apiToken = environment.getProperty("API_TOKEN");
		return UriComponentsBuilder.fromHttpUrl(url).queryParam("api_token", apiToken).build().toUriString();
	}

	private HttpHeaders getTinyUrlAPIHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	public ResponseEntity<ResponseData> shortenUrl(String authToken, TinyUrlRequest tinyUrlRequest) {
		String baseUrl = environment.getProperty("BASE_URL");
		String url = addAPIKeyInQueryParam(baseUrl);
		HttpHeaders header = getTinyUrlAPIHeaders();
		HttpEntity<TinyUrlRequest> entity = new HttpEntity<>(tinyUrlRequest, header);
		ResponseEntity<TinyUrlResponse> response = null;
		ResponseData responseData = null;
		try {
			response = restTemplate.postForEntity(url, entity, TinyUrlResponse.class);
		} catch (HttpClientErrorException e) {
			responseData = ResponseHelper.responseSender(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ResponseData>(responseData, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			responseData = ResponseHelper.responseSender(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ResponseData>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String userName = jwtUtil.extractUsername(authToken);
		UserData user = userDataRepository.findByUserName(userName);
		ApiLog apiLog = new ApiLog();
		apiLog.setEndUrl(url);
		apiLog.setRequest(objectToString(tinyUrlRequest));
		apiLog.setRequestTime(LocalDateTime.now());
		apiLog.setResponse(objectToString(response));
		apiLog.setResponseTime(LocalDateTime.now());
		apiLog.setCreatedAt(LocalDateTime.now());
		apiLog.setUpdatedAt(LocalDateTime.now());
		apiLog.setUser(user);
		apiLogRepository.save(apiLog);

		if (response.getBody().getCode() == 0) {
			responseData = ResponseHelper.responseSender("Success", HttpStatus.OK.value(),
					response.getBody().getData().getTiny_url());
			return new ResponseEntity<ResponseData>(responseData, HttpStatus.OK);
		}
		responseData = ResponseHelper.responseSender("Unable to Process Error",
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ResponseData>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public static String objectToString(Object object) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonString = "";
		try {
			jsonString = ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}
