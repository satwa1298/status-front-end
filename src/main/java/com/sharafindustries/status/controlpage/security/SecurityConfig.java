//package com.sharafindustries.status.controlpage.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter
//{
//	@Autowired
//	private BackendAuthenticationProvider backendAuthenticationProvider;
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception
//	{
//		auth.authenticationProvider(backendAuthenticationProvider);
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception
//	{
//		http.authorizeRequests()
//			.antMatchers("/register", "/confirmation", "/login").permitAll()
//			.anyRequest().authenticated()
//			.and()
//			.oauth2Login();
//	}
//}
