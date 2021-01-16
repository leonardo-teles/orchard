package com.orchard.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.orchard.model.Role;
import com.orchard.model.User;

public interface AccountService {

	public User saveUser(String name, String username, String email);
	
	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
	public List<User> userList();
	
	public Role findUserRoleByName(String role);
	
	public Role saveRole(Role role);
	
	public void updateUserPassword(User user, String newPassword);
	
	public User updateUser(User user, HashMap<String, String> request);
	
	public User simpleSave(User user);
	
	public User findById(Integer id);
	
	public void deleteUser(User user);
	
	public void resetPassword(User user);
	
	public List<User> getUsersListByUsername(String username);
	
	public String saveUserImage(MultipartFile multipartFile, Integer userImageId);
	
}
