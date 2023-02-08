
package com.mvnproj.urlshortening.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataRequest {

	@NotBlank(message = "Username may not be blank")
	private String username;

	@Pattern(regexp = "^\\d{10}$", message = "Invalid phone number entered")
	private String phone;

	@NotBlank(message = "Email may not be blank")
	@Email(message = "Provide valid email id")
	private String email;

	@Size(max = 16, min = 8, message = "Password must be between 8 and 16 characters in length")
	private String password;

}
