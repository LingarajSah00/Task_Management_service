package com.firstgenix.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.firstgenix.security.model.Authority;
import com.firstgenix.security.model.User;
import com.firstgenix.security.repository.AuthorityRepository;
import com.firstgenix.security.repository.UserRepository;

@Service
public class SuperAdminService {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	

	public User saveSuperAdmin(User user) {
	
		User userInstance = new User();
		List<Authority> authorities = new ArrayList<Authority>();
//		userInstance.setFirstname(user.getFirstname());
//		userInstance.setLastname(user.getLastname());
//		userInstance.setMiddlename(user.getMiddlename());
		userInstance.setUsername(user.getUsername());
//		userInstance.setEmail(user.getEmail());

		if (user.getAuthorities() != null) {
			for (int i = 0; i < user.getAuthorities().size(); i++) {

				Authority authority = authorityRepository.findById(user.getAuthorities().get(i).getId()).get();
				authorities.add(authority);
				userInstance.setAuthorities(authorities);
			}
		}
		// userInstance.setAuthorities(authorities);
		userInstance.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userInstance.setEnabled(true);
		userInstance.setLastPasswordResetDate(new Date());
		return userRepository.save(userInstance);
	}

}
