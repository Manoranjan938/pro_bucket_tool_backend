package com.tool.ProjectTool.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool.ProjectTool.model.request.ProjectRequest;
import com.tool.ProjectTool.model.response.ProjectResponse;
import com.tool.ProjectTool.service.ProjectService;
import com.tool.ProjectTool.service.ValidationErrorService;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ValidationErrorService errorService;
	
	@PostMapping("/createProject")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody ProjectRequest request, BindingResult result, 
											Principal principal){
		
		ResponseEntity<?> errorMap = errorService.mapValidationError(result);
		if(errorMap != null) {
			return errorMap;
		}
		
		String project = projectService.saveOrUpdateProejct(request, principal.getName());
		
		return new ResponseEntity<String>(project, HttpStatus.CREATED);
	}
	
	@GetMapping("/getProjects")
	public ResponseEntity<?> getAllProjects(){
		
		List<ProjectResponse> projects = projectService.getAllProjects();
		
		return new ResponseEntity<List<ProjectResponse>>(projects, HttpStatus.OK);
		
	}
	
}
