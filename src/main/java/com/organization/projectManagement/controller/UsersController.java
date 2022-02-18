package com.organization.projectManagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organization.projectManagement.model.request.PasswordChangeRequest;
import com.organization.projectManagement.model.request.UpdateUserRequest;
import com.organization.projectManagement.model.response.UserResponse;
import com.organization.projectManagement.service.UserService;
import com.organization.projectManagement.service.ValidationErrorService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("https://localhost:3002")
public class UsersController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ValidationErrorService errorService;
	
	@GetMapping("/me/{userId}")
	public ResponseEntity<?> getUserDetails(@PathVariable("userId") long userId){
		
		UserResponse userDetails = userService.getUserDetails(userId);
		
		return new ResponseEntity<UserResponse>(userDetails, HttpStatus.OK);
	}
	
	@PostMapping("/updateMe")
	public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserRequest updateUser){
		
		String response = userService.updateUser(updateUser);
		
		return  new ResponseEntity<String>(response, HttpStatus.CREATED);
	}
	
	
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordChangeRequest requestPwd, 
													BindingResult bindResult){
		
		ResponseEntity<?> errorMap = errorService.mapValidationError(bindResult);
		if(errorMap != null) {
			return errorMap;
		}
		
		String pwdStatus = userService.updateOldPassword(requestPwd);
		
		return new ResponseEntity<String>(pwdStatus, HttpStatus.CREATED);
	}

}
