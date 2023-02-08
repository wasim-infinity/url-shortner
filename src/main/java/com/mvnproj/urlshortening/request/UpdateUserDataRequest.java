
package com.mvnproj.urlshortening.request;

import lombok.Data;

@Data
public class UpdateUserDataRequest {

	private long id;
	private String username;
	private String phone;
	private String email;
	private String password;

}
