package com.orchard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
	public User findUserById(Integer id);
	
	public List<User> findByUsernameContainig(String username);
}
