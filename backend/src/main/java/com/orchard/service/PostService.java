package com.orchard.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orchard.model.Post;
import com.orchard.model.User;

public interface PostService {

	public Post savePost(User user, HashMap<String, String> request, String postImageName);
	
	public List<Post> postList();
	
	public Post getPostById(Long id);
	
	public List<Post> findPostByUsername(String username);
	
	public Post deletePost(Post post);
	
	public String savePostImage(HttpServletRequest request, String fileName);
}
