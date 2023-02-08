package com.mvnproj.urlshortening.response;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.mvnproj.urlshortening.entity.UserData;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ResponseData {

	@JsonView({CreateView.class, AuthenticateView.class, ShortenUrlView.class })
	private String statusMessage;
	
	@JsonView({CreateView.class, AuthenticateView.class, ShortenUrlView.class })
	private int statusCode;

	@JsonView({ UserListView.class })
	private List<UserData> userData;

	@JsonView({ UserDataView.class })
	private UserData user;

	@JsonView({ AuthenticateView.class })
	private String userName;

	@JsonView({ AuthenticateView.class })
	private String authToken;

	@JsonView({ ShortenUrlView.class })
	private String tinyUrl;

	public interface UserListView {

	}

	public interface UserDataView {

	}

	public interface AuthenticateView {

	}

	public interface ShortenUrlView {

	}
	
	public interface CreateView{
		
	}

}
