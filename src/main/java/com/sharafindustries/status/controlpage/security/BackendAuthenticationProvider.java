package com.sharafindustries.status.controlpage.security;

import java.util.ArrayList;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.sharafindustries.status.controlpage.client.StatusClient;

@Component
public class BackendAuthenticationProvider implements AuthenticationProvider
{

	@Autowired
	private StatusClient statusClient;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		System.out.println("in authentication provider");
		if (authentication == null)
		{
			System.out.println("authentication is null");
			return null;
		}
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		System.out.println("CREDENTIALS " + username + " " + password);
		String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		System.out.println("AUTH HEADER " + authHeader);
		try
		{
			statusClient.authenticateUser(authHeader);
		}
		catch (Exception e)
		{
			throw new BadCredentialsException("External authentication failed", e);
		}

		return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
