package com.sharafindustries.status.controlpage.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sharafindustries.status.controlpage.client.StatusClient;

@Component
public class CustomOAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	@Autowired
	private StatusClient client;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
	{
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();
        String idToken = oidcUser.getIdToken().getTokenValue();
        //TODO use auth service for this once made
        boolean hasAccount = client.doesAccountExist("Bearer " + idToken);
        if (!hasAccount)
        	response.sendRedirect("/signup");
        else 
        	response.sendRedirect("/dashboard");
	}

}
