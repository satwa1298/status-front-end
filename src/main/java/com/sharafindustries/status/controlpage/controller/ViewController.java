package com.sharafindustries.status.controlpage.controller;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sharafindustries.status.controlpage.client.StatusClient;
import com.sharafindustries.status.controlpage.model.UserStatusInfo;

import feign.FeignException.FeignClientException;

@Controller
public class ViewController
{
	@Autowired
	private StatusClient statusClient;
	
	@GetMapping("/register")
	public String toRegisterPage()
	{
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model)
	{
		try
		{
			ResponseEntity<String> response = statusClient.createUser(email, password);
			if (response.getStatusCode() == HttpStatus.CREATED)
			{
				model.addAttribute("email", email);
				return "confirmation";
			}
			else
			{
				model.addAttribute("error", "Failed to register");
				return "register";
			}
		}
		catch (Exception e)
		{
			model.addAttribute("error", "An error occurred: " + e.getMessage());
			return "register";
		}
	}
	
//	@GetMapping("/login")
//	public String getLoginPage()
//	{
//		return "login";
//	}
//	
//	@PostMapping("/login")
//	public String doLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model)
//	{
//		String credentialString = email + ":" + password;
//		String authorizationHeader = "Basic " + Base64.getEncoder().encodeToString(credentialString.getBytes());
//		try
//		{
//			statusClient.authenticateUser(authorizationHeader);
//			return "redirect:/dashboard";
//		}
//		catch (FeignClientException e)
//		{
//			model.addAttribute("error", "Invalid credentials");
//            return "login";
//		}
//	}
	
	@GetMapping(value = {"/", "/dashboard"})
	public String toDashboard(Model model, @RequestHeader("Authorization") String authorizationHeader, Principal principal)
	{
		System.out.println("***** IN DASHBOARD");
		//TODO add error handling
        List<UserStatusInfo> customStatuses = statusClient.getCustomStatuses(authorizationHeader);
        model.addAttribute("customStatuses", customStatuses);

        UserStatusInfo currentStatus = statusClient.getCurrentStatus(authorizationHeader, principal.getName());
        model.addAttribute("currentStatus", currentStatus);

        List<String> friendList = statusClient.getFriendList(authorizationHeader);
        model.addAttribute("friends", friendList);
		return "dashboard";
	}
	
	@PostMapping("/add-friend")
	public String addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("emailToAdd") String emailToAdd)
	{
		statusClient.addFriend(authorizationHeader, emailToAdd);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/delete-friend")
	public String deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("emailToDelete") String emailToDelete)
	{
		statusClient.deleteFriend(authorizationHeader, emailToDelete);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/create-custom-status")
	public String createCustomStatus(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam("statusName") String statusName, @RequestParam("availability") String availability,
			@RequestParam("message") String message)
	{
		statusClient.createCustomStatus(authorizationHeader, statusName, availability, message);
		System.out.println("**** END CREATE CUSTOM STATUS");
		return "redirect:/dashboard";
	}
	
	@PostMapping("/delete-custom-status")
	public String deleteCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("statusName") String statusName)
	{
		statusClient.deleteCustomStatus(authorizationHeader, statusName);
		return "redirect:/dashboard";
	}
	
	@PostMapping("/set-current-status")
	public String setCurentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("statusName") String statusName, Model model)
	{
		try
		{
			statusClient.setCurrentStatus(authorizationHeader, statusName);
			System.out.println("**** END SET STATUS");
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
	
}
