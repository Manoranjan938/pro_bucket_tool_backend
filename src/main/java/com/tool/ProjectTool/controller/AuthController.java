package com.tool.ProjectTool.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool.ProjectTool.entity.Users;
import com.tool.ProjectTool.model.request.LoginRequest;
import com.tool.ProjectTool.model.request.UserRequest;
import com.tool.ProjectTool.model.response.LoginResponse;
import com.tool.ProjectTool.security.SecurityConstants;
import com.tool.ProjectTool.security.TokenProvider;
import com.tool.ProjectTool.service.UserService;
import com.tool.ProjectTool.service.ValidationErrorService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private ValidationErrorService errorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/newUser")
	public ResponseEntity<?> createLocalUser(@Valid @RequestBody UserRequest user, BindingResult result){
		
		try {
			ResponseEntity<?> errorMap = errorService.mapValidationError(result);
			if(errorMap != null) {
				return errorMap;
			}
			
			Users user1 = userService.createUser(user);
			
			return new ResponseEntity<Users>(user1, HttpStatus.CREATED);
		}
		catch(Exception e) {
			return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest logRequest){
		
		Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(logRequest.getUsername(), logRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(auth);
		
		return ResponseEntity.ok(new LoginResponse(true, jwt));
	}
	
}
