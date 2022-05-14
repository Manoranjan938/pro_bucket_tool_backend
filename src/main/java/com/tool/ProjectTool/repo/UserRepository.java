package com.tool.ProjectTool.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool.ProjectTool.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	public Users findByUsername(String username);
	
	public Users findByUserId(String userId);
	
}
