package com.tool.ProjectTool.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ProjectTool.entity.Backlog;
import com.tool.ProjectTool.entity.ProjectTask;
import com.tool.ProjectTool.entity.Users;
import com.tool.ProjectTool.exception.ProjectNotFoundException;
import com.tool.ProjectTool.exception.TaskIdNotFoundException;
import com.tool.ProjectTool.model.request.ProjectTaskRequest;
import com.tool.ProjectTool.model.request.RequestCreateSubtask;
import com.tool.ProjectTool.model.request.UpdateProjectTaskRequest;
import com.tool.ProjectTool.model.response.SubtaskList;
import com.tool.ProjectTool.model.response.TaskDetails;
import com.tool.ProjectTool.model.response.TaskListResponse;
import com.tool.ProjectTool.repo.BacklogRepository;
import com.tool.ProjectTool.repo.ProjectTaskRepository;
import com.tool.ProjectTool.repo.UserRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepo;

	@Autowired
	private ProjectTaskRepository projectTaskRepo;

	@Autowired
	private UserRepository userRepo;

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
			projTask.setTaskName(request.getTaskName());
			;
			projTask.setSprintId(request.getSprintId());
			projTask.setSubtask(false);

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

				tas.setTaskName(project.getTaskName());
				tas.setTaskSequence(project.getProjectSequence());
				tas.setPriority(project.getPriority());
				tas.setStatus(project.getStatus());

				taskLists.add(tas);
			}

			return taskLists;
		}

		throw new TaskIdNotFoundException("Task not found");

	}

	public List<SubtaskList> getSubTaskList(String parentId) {

		List<Object> subtaskList = projectTaskRepo.findSubtaskListByParentId(parentId);
		List<SubtaskList> subtaskLists = new ArrayList<>();
		Iterator itr = subtaskList.iterator();

		while (itr.hasNext()) {
			Object[] row = (Object[]) itr.next();
			SubtaskList sub = new SubtaskList();

			sub.setTaskName(String.valueOf(row[0]));
			sub.setTaskType(String.valueOf(row[1]));
			sub.setTaskSequence(String.valueOf(row[2]));
			sub.setPriority(String.valueOf(row[3]));
			sub.setStatus(String.valueOf(row[4]));
			sub.setAssignee(String.valueOf(row[5]));

			subtaskLists.add(sub);

		}

		return subtaskLists;
	}

	public TaskDetails getTaskDetails(String taskSequence) {

		ProjectTask task = projectTaskRepo.findByProjectSequence(taskSequence);
		if (task != null) {

			TaskDetails getTask = new TaskDetails();
			Users user = userRepo.findByUserId(task.getAssignee());

			if (user != null) {
				getTask.setAssignee(user.getName());
			}

			getTask.setTaskName(task.getTaskName());
			getTask.setTaskDesc(task.getTaskDesc());
			getTask.setTaskSequence(task.getProjectSequence());
			getTask.setPriority(task.getPriority());
			getTask.setAssignee(null);
			getTask.setStatus(task.getStatus());
			getTask.setCreatedOn(task.getCreatedAt());
			getTask.setUpdatedOn(task.getUpdatedAt());

			return getTask;
		}

		throw new TaskIdNotFoundException("Task sequence not found");
	}

	public String updateTask(UpdateProjectTaskRequest updateTask) {

		ProjectTask task = projectTaskRepo.findByProjectSequence(updateTask.getTaskId());

		if (task != null) {

			task.setAssignee(updateTask.getAssignee());
			task.setPriority(updateTask.getPriority());
			task.setTaskDesc(updateTask.getDescription());
			task.setStatus(updateTask.getStatus());

			projectTaskRepo.save(task);
			return "Task updated successfully";

		}

		throw new TaskIdNotFoundException("Task id not found");
	}

	public String createSubtask(RequestCreateSubtask subtaskRequest) {

		try {
			Backlog back = backlogRepo.findByProjectIdentifier(subtaskRequest.getProjectId());

			ProjectTask projTask = new ProjectTask();
			projTask.setBacklog(back);

			Integer backlogSequesnce = back.getPTSequence();
			backlogSequesnce++;
			back.setPTSequence(backlogSequesnce);

			projTask.setProjectSequence(subtaskRequest.getProjectId() + "-" + backlogSequesnce);
			projTask.setProjectIdentifier(subtaskRequest.getProjectId());

			if (subtaskRequest.getPriority() == "" || subtaskRequest.getPriority() == null) {
				projTask.setPriority("LOW");
			}
			if (subtaskRequest.getStatus() == "" || subtaskRequest.getStatus() == null) {
				projTask.setStatus("TODO");
			}

			projTask.setPriority(subtaskRequest.getPriority());
			projTask.setTaskName(subtaskRequest.getTaskName());
			;
			// projTask.setSprintId(request.getSprintId());
			projTask.setSubtask(true);
			projTask.setTaskType("Task");
			projTask.setParentTaskId(subtaskRequest.getParentTaskId());

			projectTaskRepo.save(projTask);

			return "Task created successfully";
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not found");
		}

	}

}
