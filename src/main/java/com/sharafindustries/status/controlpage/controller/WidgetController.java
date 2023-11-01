package com.sharafindustries.status.controlpage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharafindustries.status.controlpage.client.StatusClient;
import com.sharafindustries.status.controlpage.model.UserStatusInfo;

@RestController
@RequestMapping("/widget")
public class WidgetController
{
	//TODO error handling for api calls
	
	@Autowired
	private StatusClient statusClient;
	
	@GetMapping("/get-custom-statuses")
	public List<UserStatusInfo> getCustomStatuses(@RequestHeader("Authorization") String authorizationHeader)
	{
		return statusClient.getCustomStatuses("Passphrase " + authorizationHeader);
	}
	
	@PostMapping("/set-status")
	public String setStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		return statusClient.setCurrentStatus("Passphrase " + authorizationHeader, body);
	}
	
	@GetMapping("/get-status")
	public UserStatusInfo getStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("email") String email)
	{
		return statusClient.getCurrentStatus("Passphrase " + authorizationHeader, email);
	}

}
