package com.tool.ProjectTool.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ProjectTool.entity.ProjectEntity;
import com.tool.ProjectTool.entity.Users;
import com.tool.ProjectTool.exception.UserNotFoundException;
import com.tool.ProjectTool.model.request.ProjectRequest;
import com.tool.ProjectTool.model.response.ProjectResponse;
import com.tool.ProjectTool.repo.ProjectRepository;
import com.tool.ProjectTool.repo.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private UserRepository userRepo;
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public String saveOrUpdateProejct(ProjectRequest request) {

		int status = 1;
		String ids = randomString(20);
		String id = null;

		try {

			Users user = userRepo.findByUserId(request.getUserId());
			ProjectEntity checkId = projectRepo.findByProjectId(ids);

			ProjectEntity project = new ProjectEntity();

			project.setUser(user);
			project.setProjectLeader(user.getName());
			project.setProjectName(request.getProjectName());
			project.setProjectTeamType(request.getProjectType());
			project.setProjectTemplateType(request.getProjectTemplate());

			if (request.getProjectType().equalsIgnoreCase("personal")) {
				project.setProjectAccessType("Private");
			}
			
			if (checkId != null) {
				id = randomString(20);
			} else {
				id = ids;
			}

			project.setProjectId(id);
			project.setProjectAccessType(request.getAccessType());
			project.setStatus(status);

			projectRepo.save(project);

			return "Project created";
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public List<ProjectResponse> getAllProjects(int userId) {

		List<ProjectEntity> project = projectRepo.getProjectsByUserId(userId);
		List<ProjectResponse> projectList = new ArrayList<ProjectResponse>();

		if (!project.isEmpty()) {

			for(ProjectEntity projects : project) {
				ProjectResponse response = new ProjectResponse();
				
				response.setProjectName(projects.getProjectName());
				response.setProjectType(projects.getProjectTeamType());
				response.setLeadBy(projects.getProjectLeader());
				response.setProjectAvatar(projects.getProjectImage());
				response.setProjectId(projects.getProjectId());
				
				projectList.add(response);
			}

		}

		return projectList;
	}
	
	public ProjectResponse getProjectById(String id) {
		
		ProjectEntity project = projectRepo.findByProjectId(id);
		if(project != null) {
			
			ProjectResponse response = new ProjectResponse();
			
			response.setProjectName(project.getProjectName());
			response.setProjectType(project.getProjectTeamType());
			response.setLeadBy(project.getProjectLeader());
			response.setProjectAvatar(project.getProjectImage());
			response.setProjectId(project.getProjectId());
			
			return response;
		}
		
		throw new UserNotFoundException("Project not found with id" + id);
	}

}
