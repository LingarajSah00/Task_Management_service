package com.firstgenix.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.firstgenix.security.JwtUser;
import com.firstgenix.security.JwtUserFactory;
import com.firstgenix.security.json.ExternalUser;
import com.firstgenix.security.model.Authority;
import com.firstgenix.security.model.User;
import com.firstgenix.security.repository.AuthorityRepository;
import com.firstgenix.security.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {

			return JwtUserFactory.create(user);
		}
	}
	
	public User loadUserByEmail(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {

			return user;
		}
	}

	public User saveUser(ExternalUser externalUser) {
		
		User userInstance = new User();
		List<Authority> list=new ArrayList<Authority>();
		User userExists = userRepository.findByUsername(externalUser.getUsername());
		if (userExists != null) {
			System.out.println("inside if block"+userExists.getUsername());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "USER ALREADY AVAILABLE");
		} else {
			userInstance.setUsername(externalUser.getUsername());
			userInstance.setPassword(bCryptPasswordEncoder.encode(externalUser.getPassword()));
			userInstance.setEnabled(externalUser.getEnabled());
			userInstance.setLastPasswordResetDate(new Date());
			userInstance.setOrganization(externalUser.getOrganization());
	  if(externalUser.getRole()!=null) {
		  Authority authorityExists=authorityRepository.findByName(externalUser.getRole());
		  if(authorityExists != null) {
			  list.add(authorityExists);
			   }
		  else {
			  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AUTHORITY NOT FOUND IN DB");
		  }
		  
		  }
	  userInstance.setAuthorities(list);
		 
	  }
		
		
		return userRepository.save(userInstance);

	}

	public UserDetails editUser(User user, String id) {
		Optional<User> userExists = userRepository.findById(id);
		if (userExists.isPresent()) {
			User user_new = userExists.get();
//			user_new.setFirstname(user.getFirstname());
//			user_new.setMiddlename(user.getMiddlename());
//			user_new.setLastname(user.getLastname());
			user_new.setUsername(user.getUsername());
			user_new.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			user_new.setEnabled(true);
			user_new.setLastPasswordResetDate(new Date());
			userRepository.save(user_new);
			return JwtUserFactory.create(user_new);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User availabe in this ID");

		}

	}
	
	public UserDetails setEnabled(User user, String id) {
		Optional<User> userExists = userRepository.findById(id);
		if (userExists.isPresent()) {
			User user_new = userExists.get();
			
			user_new.setEnabled(true);
			user_new.setLastPasswordResetDate(new Date());
			userRepository.save(user_new);
			return JwtUserFactory.create(user_new);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User availabe in this ID");

		}
		
	}

	public void deleteUser(String id) {
		Optional<User> userExists = userRepository.findById(id);
		if (userExists.isPresent()) {
			User user_new = userExists.get();
			user_new.setEnabled(false);
			userRepository.save(user_new);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User availabe in this ID");

		}

	  
  } 
  
  
  public void saveRole(Authority authority)   {
		 
	  Authority roleExists =authorityRepository.findByName(authority.getName());
  	     if (roleExists != null) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is already a role having with the name provided");
		  }else{
	  authorityRepository.save(authority);
}


	}



	public UserDetails getUser(String id) {
		User user_new = null;
		Optional<User> userExists = userRepository.findById(id);
		if (userExists.isPresent()) {
			user_new = userExists.get();
			
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User availabe in this ID");
		}
		return JwtUserFactory.create(user_new);
	}
	
	public UserDetails getOrganisation(String id){
		User userInstance=new User();
		 userInstance=userRepository.findByOrganization(id);
		return JwtUserFactory.create(userInstance);
		
	}

	public boolean userHasAuthority(String authority, JwtUser user) {
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (authority.equals(grantedAuthority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	public String findAuthorityName(String id) {
		String name = "";
		Authority authority = null;
		Optional<Authority> authExists = authorityRepository.findById(id);
		if (authExists.isPresent()) {
			authority = authExists.get();
			name = authority.getName();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Authority availabe in this ID");
		}
		return name;
	}

}
