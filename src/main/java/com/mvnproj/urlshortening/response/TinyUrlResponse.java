
package com.mvnproj.urlshortening.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TinyUrlResponse {

	private TinyUrlData data;
	private int code;
	private List<String> errors;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TinyUrlData {
		String domain;
		String alias;
		boolean deleted;
		boolean archived;
		Analytics analytics;
		List<String> tags;
		String created_at;
		String expires_at;
		String tiny_url;
		String url;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Analytics {
		boolean enabled;
		@JsonProperty("public")
		boolean publicValue;
	}

}
