package com.sharafindustries.status.controlpage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sharafindustries.status.controlpage.client.StatusClient;
import com.sharafindustries.status.controlpage.model.UserStatusInfo;

import feign.FeignException.FeignClientException;

@Controller
public class ViewController
{
	
	//TODO handle case where user attempts to access some url while authenticated through google but doesnt have an account 
	//TODO error handling for api calls
	//TODO add generic /error page
	
	@Autowired
	private StatusClient statusClient;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	
	@GetMapping(value = {"/", "/dashboard"})
	public String toDashboard(Model model, @AuthenticationPrincipal OAuth2User user)
	{
		String idToken = getIdToken();
		String authorizationHeader= createAuthorizationHeader(idToken);
		
		//TODO add error handling
        List<UserStatusInfo> customStatuses = statusClient.getCustomStatuses(authorizationHeader);
        model.addAttribute("customStatuses", customStatuses);
        
        UserStatusInfo currentStatus = statusClient.getCurrentStatus(authorizationHeader, user.getAttribute("email"));
        model.addAttribute("currentStatus", currentStatus);

        List<String> friendList = statusClient.getFriendList(authorizationHeader);
        model.addAttribute("friends", friendList);
		return "dashboard";
	}
	
	@PostMapping("/add-friend")
	public String addFriend(@RequestParam("emailToAdd") String emailToAdd)
	{
		String authorizationHeader = createAuthorizationHeader();
		Map<String, String> body = new HashMap<>();
		body.put("emailToAdd", emailToAdd);
		statusClient.addFriend(authorizationHeader, body);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/delete-friend")
	public String deleteFriend(@RequestParam("emailToDelete") String emailToDelete)
	{
		String authorizationHeader = createAuthorizationHeader();
		Map<String, String> body = new HashMap<>();
		body.put("emailToDelete", emailToDelete);
		statusClient.deleteFriend(authorizationHeader, body);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/create-custom-status")
	public String createCustomStatus(@RequestParam("statusName") String statusName, @RequestParam("availability") String availability, @RequestParam("message") String message)
	{
		String authorizationHeader = createAuthorizationHeader();
		Map<String, String> body = new HashMap<>();
		body.put("statusName", statusName);
		body.put("availability", availability);
		body.put("message", message);
		statusClient.createCustomStatus(authorizationHeader, body);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/delete-custom-status")
	public String deleteCustomStatus(@RequestParam("statusName") String statusName)
	{
		String authorizationHeader = createAuthorizationHeader();
		Map<String, String> body = new HashMap<>();
		body.put("statusName", statusName);
		statusClient.deleteCustomStatus(authorizationHeader, body);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/set-current-status")
	public String setCurentStatus(@RequestParam("statusName") String statusName, Model model)
	{
		try
		{
			String authorizationHeader = createAuthorizationHeader();
			Map<String, String> body = new HashMap<>();
			body.put("statusName", statusName);
			statusClient.setCurrentStatus(authorizationHeader, body);
			return "redirect:/dashboard";
		}
		catch (FeignClientException exception) 
		{
			if (exception.status() == 401)
			{
				model.addAttribute("setStatusError", "bad credentials");
			}
			else if (exception.status() == 404)
			{
				model.addAttribute("setStatusError", "status with name " + statusName + " not found");
			}
			return "dashboard";
		}
	}
	
	@GetMapping("/view-passphrase")
	public String toPassphrase(Model model)
	{
		String passphrase = statusClient.getPassphrase(createAuthorizationHeader());
		logger.info("received passphrase is {}", passphrase);
		model.addAttribute("passphrase", passphrase);
		return "passphrase";
	}
	
	@GetMapping("/signup")
	public String toSignupPage()
	{
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signup(@AuthenticationPrincipal OAuth2User user)
	{
		String authorizationHeader = createAuthorizationHeader(getIdToken());
		statusClient.createUser(authorizationHeader);
		return "redirect:/dashboard";
	}
	
	
	
	//TODO refactor these into auth service
	private String getIdToken()
	{
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

		// get access token (will need to inject OAuth2AuthorizedClientService)
		// OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
		// String accessToken = client.getAccessToken().getTokenValue();
		
		String idToken = ((DefaultOidcUser) oauthToken.getPrincipal()).getIdToken().getTokenValue();
		return idToken;
	}
	
	private String createAuthorizationHeader(String idToken)
	{
		return "Bearer " + idToken;
	}
	
	private String createAuthorizationHeader() 
	{
		return "Bearer " + getIdToken();
	}
	
}
