package com.sharafindustries.status.controlpage.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.sharafindustries.status.controlpage.model.UserStatusInfo;

@FeignClient(name = "statusClient", url = "${feign.status-client.url}")
public interface StatusClient
{
	@GetMapping("/does-account-exist")
	boolean doesAccountExist(@RequestHeader("Authorization") String authorizationHeader);
	
	@PostMapping("/create-user")
	ResponseEntity<String> createUser(@RequestHeader("Authorization") String authorizationHeader);
	
	@GetMapping("/my-statuses")
	List<UserStatusInfo> getCustomStatuses(@RequestHeader("Authorization") String authorizationHeader);
	
	@GetMapping("/get-status")
	UserStatusInfo getCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("email") String email);
	
	@GetMapping("/my-friends")
	List<String> getFriendList(@RequestHeader("Authorization") String authorizationHeader);
	
	@PostMapping("/add-friend")
	ResponseEntity<String> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body);
	
	@PostMapping("/delete-friend")
	ResponseEntity<String> deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body);
	
	@PostMapping("/create-custom-status")
	ResponseEntity<String> createCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body);
	
	@PostMapping("/delete-custom-status")
	ResponseEntity<String> deleteCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body);
	
	@PostMapping("/set-status")
	String setCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body);
	
	@GetMapping("/get-passphrase")
	String getPassphrase(@RequestHeader("Authorization") String authorizationHeader);

}
