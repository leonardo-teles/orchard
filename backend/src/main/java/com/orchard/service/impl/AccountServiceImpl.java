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

import com.orchard.model.AppUser;
import com.orchard.model.Role;
import com.orchard.model.UserRole;
import com.orchard.repository.AppUserRepository;
import com.orchard.repository.RoleRepository;
import com.orchard.service.AccountService;
import com.orchard.utility.Constants;
import com.orchard.utility.EmailConstructor;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountService accountService;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private AppUserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmailConstructor emailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	@Transactional
	public AppUser saveUser(String name, String username, String email) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = encoder.encode(password);
		
		AppUser appUser = new AppUser();
		appUser.setPassword(encryptedPassword);
		appUser.setName(name);
		appUser.setUsername(username);
		appUser.setEmail(email);
		
		Set<UserRole> userRoles = new HashSet<>();
		
		userRoles.add(new UserRole(appUser, accountService.findUserRoleByName("USER")));
		appUser.setUserRoles(userRoles);
		
		userRepository.save(appUser);
		
		byte[] bytes;
		
		try {
			bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
			String fileName = appUser.getId() + ".png";
			Path path = Paths.get(Constants.USER_FOLDER + fileName);
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mailSender.send(emailConstructor.newUserEmail(appUser, password));
		
		return appUser;
	}

	@Override
	public void updateUserPassword(AppUser appUser, String newpassword) {
		String encryptedPassword = encoder.encode(newpassword);
		appUser.setPassword(encryptedPassword);
		
		userRepository.save(appUser);
		
		mailSender.send(emailConstructor.resetPasswordEmail(appUser, newpassword));
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}
	
	@Override
	public AppUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public AppUser findByEmail(String userEmail) {
		return userRepository.findByEmail(userEmail);
	}

	@Override
	public List<AppUser> userList() {
		return userRepository.findAll();
	}

	@Override
	public Role findUserRoleByName(String name) {
		return roleRepository.findRoleByName(name);
	}
	
	@Override
	public AppUser simpleSaveUser(AppUser user) {
		userRepository.save(user);
		mailSender.send(emailConstructor.updateUserEmail(user));
		
		return user;
	}

	@Override
	public AppUser updateUser(AppUser user, HashMap<String, String> request) {
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
	public AppUser findUserById(Integer id) {
		return userRepository.findUserById(id);
	}

	@Override
	public void deleteUser(AppUser appUser) {
		userRepository.delete(appUser);

	}

	@Override
	public void resetPassword(AppUser user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = encoder.encode(password);
		user.setPassword(encryptedPassword);
		
		userRepository.save(user);
		mailSender.send(emailConstructor.resetPasswordEmail(user, password));
	}

	@Override
	public List<AppUser> getUsersListByUsername(String username) {
		return userRepository.findByUsernameContaining(username);
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
			return "User picture Saved";
		}
	}
}
