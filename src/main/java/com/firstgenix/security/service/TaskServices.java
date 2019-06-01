package com.firstgenix.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firstgenix.security.model.Task;
import com.firstgenix.security.repository.TaskRepository;

@Service
public class TaskServices {
	@Autowired
	TaskRepository taskRepository;
	
	public Task createTask(Task task) {
		
		taskRepository.save(task);
		return task;
		
		
	}
	
	
	public Task UpdateTask(Task task,String id) {
		
		Optional<Task> taskInstance=taskRepository.findById(id);
		Task tasks=null;
		if(taskInstance.isPresent()) {
			tasks=taskInstance.get();
			tasks.setDate(task.getDate());
			tasks.setDescription(task.getDescription());
			tasks.setStartTime(task.getStartTime());
			tasks.setEndTime(task.getEndTime());
			taskRepository.save(tasks);
			
		}
		
		return tasks;
		
	}
	
	
	public void DeleteTask(String id) {
		
		Optional<Task> task=taskRepository.findById(id);
		Task tasks=task.get();
		taskRepository.delete(tasks);
		 
		
		
		
	}
	
	public List<Task> searchPropertyManager(String... name) {
		if (name != null && name[0] != null) {
			return taskRepository.findByDateContainingIgnoreCase(name[0]);
		} else {
			return (List<Task>) taskRepository.findAll();
		}
	}

}
