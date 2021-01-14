package com.orchard.service;

import java.util.List;

import com.orchard.model.Role;
import com.orchard.model.User;

public interface AccountService {

	public void saveUser(User user);
	
	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
	public List<User> userList();
	
	public Role findUserRoleByName(String role);
	
	public Role saveRole(Role role);
	
	public void updateUser(User user);
	
	public User findById(Integer id);
	
	public void deleteUser(User user);
	
	public void resetPassword(User user);
	
	public List<User> getUserListByUsername(String username);
	
	public void simpleSave(User user);
}
