package com.organization.projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.organization.projectManagement.entity.ProjectEntity;
import com.organization.projectManagement.entity.User;
import com.organization.projectManagement.model.request.ProjectRequest;
import com.organization.projectManagement.repo.ProjectRepository;
import com.organization.projectManagement.repo.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public String saveOrUpdateProejct(ProjectRequest request, String username) {
		
		int status = 1;
		
		try {
			
			User user = userRepo.findByUsername(username);
			
			ProjectEntity project = new ProjectEntity();
			
			project.setUser(user);
			project.setProjectLeader(user.getName());
			project.setProjectName(request.getProjectName());
			project.setProjectTeamType(request.getProjectType());
			project.setProjectTemplateType(request.getProjectTemplate());
			
			if(request.getProjectType() == "Personal") {
				project.setProjectAccessType("Private");
			}
			
			project.setProjectAccessType(request.getAccessType());
			project.setStatus(status);
			
			projectRepo.save(project);
			
			return "Project created";
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
