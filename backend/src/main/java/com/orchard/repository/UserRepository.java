package com.orchard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.id = :id")
	public User findUserById(@Param("id") Integer id);
	
	public List<User> findByUsernameContainig(String username);
}
