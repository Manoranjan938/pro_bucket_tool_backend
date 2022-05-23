package com.tool.ProjectTool.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool.ProjectTool.model.request.RequestTeamMember;
import com.tool.ProjectTool.service.ValidationErrorService;

@RestController
@RequestMapping("/api/v1/team")
public class TeamController {
	
	@Autowired
	private ValidationErrorService errorService;
	
	@PostMapping("/newTeamMember")
	public ResponseEntity<?> addNewTeamMember(@Valid @RequestBody RequestTeamMember teamMember, BindingResult result){
		
		try {
			
			ResponseEntity<?> errorMap = errorService.mapValidationError(result);
			if (errorMap != null) {
				return errorMap;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
