package com.orchard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orchard.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("SELECT p FROM Post p ORDER BY p.postedDate DESC")
	public List<Post> findAll();
	
	@Query("SELECT p FROM Post p WHERE p.username = :username ORDER BY p.postedDate DESC")
	public List<Post> findByUsername(@Param("username") String username);
	
	@Query("SELECT p FROM Post p WHERE p.id = :id")
	public Post findPostById(@Param("id") Integer id);
	
	@Modifying
	@Query("DELETE Post WHERE id = :id")
	public void deletePostById(@Param("id") Integer id);
}
