package com.tool.ProjectTool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ProjectTool.entity.ProjectEntity;
import com.tool.ProjectTool.entity.Users;
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

	public String saveOrUpdateProejct(ProjectRequest request, String username) {

		int status = 1;

		try {

			Users user = userRepo.findByUsername(username);

			ProjectEntity project = new ProjectEntity();

			project.setUser(user);
			project.setProjectLeader(user.getName());
			project.setProjectName(request.getProjectName());
			project.setProjectTeamType(request.getProjectType());
			project.setProjectTemplateType(request.getProjectTemplate());

			if (request.getProjectType() == "Personal") {
				project.setProjectAccessType("Private");
			}

			project.setProjectAccessType(request.getAccessType());
			project.setStatus(status);

			projectRepo.save(project);

			return "Project created";
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public List<ProjectResponse> getAllProjects() {

		List<ProjectEntity> project = projectRepo.findAll();

		if (!project.isEmpty()) {

			System.out.println("Hello");

		}

		return null;
	}

}
