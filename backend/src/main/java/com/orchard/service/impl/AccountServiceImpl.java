package com.orchard.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.orchard.model.Role;
import com.orchard.model.User;
import com.orchard.repository.RoleRepository;
import com.orchard.repository.UserRepository;
import com.orchard.service.AccountService;
import com.orchard.utility.EmailConstructor;

public class AccountServiceImpl implements AccountService {

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmailConstructor emailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void saveUser(User user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = encoder.encode(password);
		user.setPassword(encryptedPassword);
		
		userRepository.save(user);
		
		mailSender.send(emailConstructor.newUserEmail(user, password));
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> userList() {
		return userRepository.findAll();
	}

	@Override
	public Role findUserRoleByName(String name) {
		return roleRepository.findRoleByName(name);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public void updateUser(User user) {
		String password = user.getPassword();
		String encryptedPassword = encoder.encode(password);
		user.setPassword(encryptedPassword);
		
		userRepository.save(user);
		
		mailSender.send(emailConstructor.updateUserEmail(user));
	}

	@Override
	public User findById(Integer id) {
		return userRepository.findUserById(id);
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	public void resetPassword(User user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = encoder.encode(password);
		user.setPassword(encryptedPassword);
		
		userRepository.save(user);
		
		mailSender.send(emailConstructor.resetPasswordEmail(user, password));
	}

	@Override
	public List<User> getUserListByUsername(String username) {
		return userRepository.findByUsernameContainig(username);
	}

	@Override
	public User simpleSave(User user) {
		userRepository.save(user);
		mailSender.send(emailConstructor.updateUserEmail(user));
		
		return user;
	}

}
