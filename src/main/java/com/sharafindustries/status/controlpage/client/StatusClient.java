package com.sharafindustries.status.controlpage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "statusClient", url = "localhost:8999")
public interface StatusClient
{
	@PostMapping("/create-user")
	ResponseEntity<String> createUser(@RequestParam("email") String email, @RequestParam("password") String password);
	
	@PostMapping("/authenticate-user")
	ResponseEntity<String> authenticateUser(@RequestHeader("Authorization") String authorizationHeader);

}
