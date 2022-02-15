package com.organization.projectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organization.projectManagement.model.request.UpdateUserRequest;
import com.organization.projectManagement.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("https://localhost:3002")
public class UsersController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/me")
	public ResponseEntity<?> getUserDetails(){
		
		return null;
	}
	
	@PostMapping("/updateMe")
	public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserRequest updateUser){
		
		String response = userService.updateUser(updateUser);
		
		return  new ResponseEntity<String>(response, HttpStatus.CREATED);
	}

}
