package com.tool.ProjectTool.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tool.ProjectTool.entity.ProjectTask;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

	List<ProjectTask> findByProjectIdentifierOrderByPriority(String projectIdentifier);
	
	ProjectTask findByProjectSequence(String projectSequence);
	
	@Query(value = "select\r\n"
			+ "	task_name,\r\n"
			+ "	task_type,\r\n"
			+ "	project_sequence,\r\n"
			+ "	priority,\r\n"
			+ "	status,\r\n"
			+ "	assignee\r\n"
			+ "from\r\n"
			+ "	project_task pt\r\n"
			+ "where\r\n"
			+ "	subtask = true\r\n"
			+ "	and parent_task_id =:id", nativeQuery = true)
	List<Object> findSubtaskListByParentId(String id);
	
}
