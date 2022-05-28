package com.tool.ProjectTool.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ProjectTool.entity.Backlog;
import com.tool.ProjectTool.entity.ProjectTask;
import com.tool.ProjectTool.exception.ProjectNotFoundException;
import com.tool.ProjectTool.exception.TaskIdNotFoundException;
import com.tool.ProjectTool.model.request.ProjectTaskRequest;
import com.tool.ProjectTool.model.response.TaskDetails;
import com.tool.ProjectTool.model.response.TaskListResponse;
import com.tool.ProjectTool.repo.BacklogRepository;
import com.tool.ProjectTool.repo.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepo;

	@Autowired
	private ProjectTaskRepository projectTaskRepo;

	public String addProjectTask(ProjectTaskRequest request) {

		try {
			Backlog back = backlogRepo.findByProjectIdentifier(request.getProjectIdentifier());

			ProjectTask projTask = new ProjectTask();
			projTask.setBacklog(back);

			Integer backlogSequesnce = back.getPTSequence();
			backlogSequesnce++;
			back.setPTSequence(backlogSequesnce);

			projTask.setProjectSequence(request.getProjectIdentifier() + "-" + backlogSequesnce);
			projTask.setProjectIdentifier(request.getProjectIdentifier());

			if (request.getPriority() == "" || request.getPriority() == null) {
				projTask.setPriority("LOW");
			}
			if (request.getStatus() == "" || request.getStatus() == null) {
				projTask.setStatus("TODO");
			}

			projTask.setPriority(request.getPriority());
			projTask.setTaskDesc(request.getTaskName());

			projectTaskRepo.save(projTask);

			return "Task created successfully";
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not found");
		}
	}

	public List<TaskListResponse> getTaskLists(String backlogId) {

		List<ProjectTask> tasks = projectTaskRepo.findByProjectIdentifierOrderByPriority(backlogId);
		if (!tasks.isEmpty()) {
			List<TaskListResponse> taskLists = new ArrayList<>();
			for (ProjectTask project : tasks) {
				TaskListResponse tas = new TaskListResponse();

				tas.setTaskName(project.getTaskDesc());
				tas.setTaskSequence(project.getProjectSequence());
				tas.setPriority(project.getPriority());
				tas.setStatus(project.getStatus());

				taskLists.add(tas);
			}

			return taskLists;
		}
		
		throw new TaskIdNotFoundException("Task not found");
		
	}

	public TaskDetails getTaskDetails(String taskSequence) {
		
		ProjectTask task = projectTaskRepo.findByProjectSequence(taskSequence);
		if(task != null) {
			
			TaskDetails getTask = new TaskDetails();
			getTask.setTaskName(task.getTaskDesc());
			getTask.setTaskSequence(task.getProjectSequence());
			getTask.setPriority(task.getPriority());
			getTask.setStatus(task.getStatus());
			getTask.setCreatedOn(task.getCreatedAt());
			getTask.setUpdatedOn(task.getUpdatedAt());
			
			return getTask;
		}

		throw new TaskIdNotFoundException("Task sequence not found");
	}

}
