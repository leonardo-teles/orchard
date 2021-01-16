package com.orchard.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.orchard.model.Role;
import com.orchard.model.User;
import com.orchard.model.UserRole;
import com.orchard.repository.RoleRepository;
import com.orchard.repository.UserRepository;
import com.orchard.service.AccountService;
import com.orchard.utility.Constants;
import com.orchard.utility.EmailConstructor;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountService accountService;

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
	@Transactional
	public User saveUser(String name, String username, String email) {
		
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = encoder.encode(password);
		
		User user = new User();
		user.setPassword(encryptedPassword);
		user.setName(name);
		user.setUsername(username);
		user.setEmail(email);
		
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, accountService.findUserRoleByName("USER")));
		user.setUserRoles(userRoles);
		
		userRepository.save(user);
		byte[] bytes;
		
		try {
			bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
			String fileName = user.getId() + ".png";
			Path path = Paths.get(Constants.USER_FOLDER + fileName);
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mailSender.send(emailConstructor.newUserEmail(user, password));
		return user;
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
	public User updateUser(User user, HashMap<String, String> request) {
		String name = request.get("name");
		String email = request.get("email");
		String bio = request.get("bio");
		user.setName(name);
		user.setEmail(email);
		user.setBio(bio);

		userRepository.save(user);
		
		mailSender.send(emailConstructor.updateUserEmail(user));
		
		return user;
	}
	
	@Override
	public void updateUserPassword(User user, String newPassword) {
		String encryptedPassword = encoder.encode(newPassword);
		user.setPassword(encryptedPassword);
		
		userRepository.save(user);
		
		mailSender.send(emailConstructor.resetPasswordEmail(user, newPassword));
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

	@Override
	public String saveUserImage(MultipartFile multipartFile, Integer userImageId) {
		byte[] bytes;
		
		try {
			Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + userImageId + ".png"));
			bytes = multipartFile.getBytes();
			Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
			Files.write(path, bytes);
			
			return "User picture saved to server";
		} catch (IOException e) {
			return "User picture saved";
		}
	}
}
