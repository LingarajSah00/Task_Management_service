package com.firstgenix.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstgenix.security.model.Task;

public interface TaskRepository extends JpaRepository<Task, String>{

	List<Task> findByDateContainingIgnoreCase(String string);

}
