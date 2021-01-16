package com.orchard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orchard.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
