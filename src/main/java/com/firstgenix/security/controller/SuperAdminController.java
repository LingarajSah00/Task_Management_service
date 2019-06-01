package com.firstgenix.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.firstgenix.security.model.User;
import com.firstgenix.security.service.SuperAdminService;

@RestController
public class SuperAdminController {
	@Autowired
	SuperAdminService superAdminService;
	
	@RequestMapping(value = "${jwt.route.superadmin.path}", method = RequestMethod.POST)
	public ResponseEntity<User> registration(@RequestBody User user) {
		User userInstance = superAdminService.saveSuperAdmin(user);
		return new ResponseEntity<User>(userInstance, HttpStatus.OK);
	}

}
