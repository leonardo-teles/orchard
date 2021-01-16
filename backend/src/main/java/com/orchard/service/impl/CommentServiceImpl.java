package com.orchard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchard.model.Comment;
import com.orchard.repository.CommentRepository;
import com.orchard.service.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public void saveComment(Comment comment) {
		commentRepository.save(comment);
	}

}
