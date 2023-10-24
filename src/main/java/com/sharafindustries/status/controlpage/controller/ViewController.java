package com.sharafindustries.status.controlpage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sharafindustries.status.controlpage.client.StatusClient;

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
	
	@GetMapping("/dashboard")
	public String toDashboard()
	{
		return "dashboard";
	}
	
}
