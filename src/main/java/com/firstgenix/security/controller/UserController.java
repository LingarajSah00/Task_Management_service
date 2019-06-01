package com.firstgenix.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.firstgenix.security.model.Authority;
import com.firstgenix.security.service.JwtUserDetailsService;
@RestController
@CrossOrigin(origins = { "http://localhost:4200"}, maxAge = 6000)
public class UserController {
 
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    
    @RequestMapping(value = "${jwt.route.userrole.path}",method = RequestMethod.POST)
    public ResponseEntity<Authority> createRole(@RequestBody Authority authority)  {

  	 ((JwtUserDetailsService)userDetailsService).saveRole(authority);
    return  new ResponseEntity<Authority>(authority, HttpStatus.OK);
    }
 
	    
}
