package com.mvnproj.urlshortening.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mvnproj.urlshortening.authentication.JwtFilter;
import com.mvnproj.urlshortening.serviceimpl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Value("${settings.cors.origins}")
	private String corsOrigin; 

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(pwdEncoder);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// Prevent the HTTP response header of "Pragma: no-cache".
		httpSecurity.cors();
		httpSecurity.csrf().disable().
		//don't authenticate this particular request
		authorizeRequests().antMatchers("/v2/api-docs", 
										"/configuration/**", 
										"/swagger*/**", 
										"/webjars/**", 
										"/api/user/create-account*/**",
										"/api/user/login*/**"
										)
		.permitAll()
	    .anyRequest().authenticated()
		.and().exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);		


		httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Server",""));		
		httpSecurity.headers().httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000);
		httpSecurity.headers().xssProtection();
		httpSecurity.headers().cacheControl().disable();

	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() 
	{

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin(corsOrigin);
		configuration.addAllowedHeader("Content-Type");
		configuration.addAllowedHeader("Authorization");
		configuration.addAllowedHeader("X-Requested-With");
		configuration.addAllowedHeader("authorization");
		configuration.addAllowedHeader("multipart/form-data");
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		configuration.setMaxAge((long) 86400);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
