package com.firstgenix.security.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.firstgenix.security.model.Task;
import com.firstgenix.security.service.TaskServices;

@RestController
public class TaskController {
	
	@Autowired
	TaskServices taskService;
	
	//createTask
	@RequestMapping(value = "${jwt.route.taskCreate.path}", method = RequestMethod.POST)
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		
		Task taskInstance=taskService.createTask(task);
		return new ResponseEntity<Task>(taskInstance, HttpStatus.OK);
		
	}
	
	//updateTask
	@RequestMapping(value = "${route.taskUpdate.path}", method = RequestMethod.PUT)
	public ResponseEntity<Task> updateTask(@RequestBody Task task,@PathVariable("id")String id) {
		
		Task taskInstance=taskService.UpdateTask(task, id);
		return new ResponseEntity<Task>(taskInstance, HttpStatus.OK);
		
	}
	
	//deleteTask
	@RequestMapping(value = "${route.taskDeleted.path}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTask(@PathVariable("id")String id) {
		
		taskService.DeleteTask(id);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	
	//searchTask
	@RequestMapping(value = "${route.serachTask.path}", method = RequestMethod.GET)
	public ResponseEntity<?> searchPropertyManager(@QueryParam("date") String date){
		List<Task>task=null;
		task=taskService.searchPropertyManager(date);
		return new ResponseEntity<List<Task>>(task, HttpStatus.OK);

		
	}

}
