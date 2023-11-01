package com.sharafindustries.status.controlpage.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
	
	@Autowired
	private CustomOAuth2LoginAuthenticationSuccessHandler customOAuth2LoginAuthenticationSuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
		 .antMatchers("/widget/*").permitAll()
		 .anyRequest().authenticated()
		 .and()
		 .csrf().ignoringAntMatchers("/widget/**")
		 .and()
		 .oauth2Login()
		 .successHandler(customOAuth2LoginAuthenticationSuccessHandler);
		return http.build();
	}
}
