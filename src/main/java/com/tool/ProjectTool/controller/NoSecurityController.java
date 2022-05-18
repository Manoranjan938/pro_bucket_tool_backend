package com.tool.ProjectTool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool.ProjectTool.service.NoSecurityService;

@RestController
@RequestMapping("/api/v1/secure")
public class NoSecurityController {
	
	@Autowired
	private NoSecurityService noSecureService;
	
	@GetMapping("/requestPasswordChange/{email}")
	public ResponseEntity<String> requestPasswordChange(@PathVariable("email") String email){
		
		//noSecureService.sendRegistrationConfirmationEmail(email);
		return new ResponseEntity<String>("Email sended", HttpStatus.OK);
	}
	
	@PostMapping("/sendOTP")
	public ResponseEntity<String> sendOTP(){
		
		String message = noSecureService.sendVerificationOTP();
		
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

}
