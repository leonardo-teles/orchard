package com.orchard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchard.model.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

	public AppUser findByUsername(String username);
	
	public AppUser findByEmail(String email);
	
	@Query("SELECT ap FROM AppUser ap WHERE ap.id = :id")
	public AppUser findUserById(@Param("id") Integer id);
	
	public List<AppUser> findByUsernameContaining(String username);
}
