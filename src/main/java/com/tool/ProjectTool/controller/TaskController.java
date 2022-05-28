package com.tool.ProjectTool.controller;

import java.util.List;

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

import com.tool.ProjectTool.model.request.ProjectTaskRequest;
import com.tool.ProjectTool.model.response.TaskDetails;
import com.tool.ProjectTool.model.response.TaskListResponse;
import com.tool.ProjectTool.service.ProjectTaskService;
import com.tool.ProjectTool.service.ValidationErrorService;

@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin("https://localhost:3002")
public class TaskController {

	@Autowired
	private ProjectTaskService taskService;

	@Autowired
	private ValidationErrorService errorService;

	@PostMapping("/createTask")
	public ResponseEntity<?> createProjectTask(@Valid @RequestBody ProjectTaskRequest taskRequest,
			BindingResult result) {

		ResponseEntity<?> errorMap = errorService.mapValidationError(result);
		if (errorMap != null) {
			return errorMap;
		}

		String message = taskService.addProjectTask(taskRequest);

		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}

	@GetMapping("/getTasks/{backlog_id}")
	public ResponseEntity<List<TaskListResponse>> getTaskLists(@PathVariable("backlog_id") String id) {

		return new ResponseEntity<List<TaskListResponse>>(taskService.getTaskLists(id), HttpStatus.OK);
	}

	@GetMapping("/getTaskDetails/{project_sequence}")
	public ResponseEntity<?> getTaskDetails(@PathVariable("project_sequence") String taskSeaquenceId) {

		return new ResponseEntity<TaskDetails>(taskService.getTaskDetails(taskSeaquenceId), HttpStatus.OK);
	}

}
