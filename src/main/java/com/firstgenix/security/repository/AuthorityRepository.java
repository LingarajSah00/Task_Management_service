package com.firstgenix.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstgenix.security.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority,String>{
	 Authority findByName(String name);
}
