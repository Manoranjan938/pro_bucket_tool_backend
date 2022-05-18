package com.tool.ProjectTool.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> createNewProject(@Valid @RequestBody ProjectRequest request, BindingResult result){
		
		ResponseEntity<?> errorMap = errorService.mapValidationError(result);
		if(errorMap != null) {
			return errorMap;
		}
		
		String project = projectService.saveOrUpdateProejct(request);
		
		return new ResponseEntity<String>(project, HttpStatus.CREATED);
	}
	
	@GetMapping("/getProjects/{userId}")
	public ResponseEntity<?> getAllProjects(@PathVariable("userId") int userId){
		
		List<ProjectResponse> projects = projectService.getAllProjects(userId);
		
		return new ResponseEntity<List<ProjectResponse>>(projects, HttpStatus.OK);
		
	}
	
	@GetMapping("/getProject/{projectId}")
	public ResponseEntity<?> getAllProjects(@PathVariable("projectId") String projectId){
		
		ProjectResponse project = projectService.getProjectById(projectId);
		
		return new ResponseEntity<ProjectResponse>(project, HttpStatus.OK);
	}
	
}
