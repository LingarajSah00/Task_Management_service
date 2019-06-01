package com.firstgenix.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstgenix.security.model.User;

public interface UserRepository extends JpaRepository<User, String>{
    User findByUsername(String username);
    User findByOrganization(String organization);
    Optional<User> findById(String id);   
   
}
