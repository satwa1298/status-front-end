package com.sharafindustries.status.controlpage.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.sharafindustries.status.controlpage.model.UserStatusInfo;

@FeignClient(name = "statusClient", url = "localhost:8999")
public interface StatusClient
{
	@PostMapping("/create-user")
	ResponseEntity<String> createUser(@RequestParam("email") String email, @RequestParam("password") String password);
	
	@PostMapping("/authenticate-user")
	ResponseEntity<String> authenticateUser(@RequestHeader("Authorization") String authorizationHeader);
	
	@GetMapping("/my-statuses")
	List<UserStatusInfo> getCustomStatuses(@RequestHeader("Authorization") String authorizationHeader);
	
	@GetMapping("/get-status")
	UserStatusInfo getCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("email") String email);
	
	@GetMapping("/my-friends")
	List<String> getFriendList(@RequestHeader("Authorization") String authorizationHeader);
	
	@PostMapping("/add-friend")
	ResponseEntity<String> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("emailToAdd") String emailToAdd);
	
	@PostMapping("/delete-friend")
	ResponseEntity<String> deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("emailToDelete") String emailToDelete);
	
	@PostMapping("/create-custom-status")
	ResponseEntity<String> createCustomStatus(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam String statusName, @RequestParam String availability,
			@RequestParam String message);
	
	@PostMapping("/delete-custom-status")
	ResponseEntity<String> deleteCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String statusName);
	
	@PostMapping("/set-status")
	String setCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("statusName") String statusName);

}
