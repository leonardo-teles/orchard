package com.orchard.resource;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orchard.model.User;
import com.orchard.service.AccountService;

@RestController
@RequestMapping(value = "/user")
public class AccountResource {

	private Integer userImageId;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private AccountService accountService;

	@GetMapping("/list")
	public ResponseEntity<?> getUsersList() {
		List<User> users = accountService.userList();
		if (users.isEmpty()) {
			return new ResponseEntity<>("No Users Found.", HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<?> getUserInfo(@PathVariable String username) {
		User user = accountService.findByUsername(username);
		if (user == null) {
			return new ResponseEntity<>("No User Found.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/findByUsername/{username}")
	public ResponseEntity<?> getUsersListByUsername(@PathVariable String username) {
		List<User> users = accountService.getUsersListByUsername(username);
		if (users.isEmpty()) {
			return new ResponseEntity<>("No Users Found.", HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String, String> request) {
		String username = request.get("username");
		if (accountService.findByUsername(username) != null) {
			return new ResponseEntity<>("usernameExist", HttpStatus.CONFLICT);
		}
		
		String email = request.get("email");
		if (accountService.findByEmail(email) != null) {
			return new ResponseEntity<>("emailExist", HttpStatus.CONFLICT);
		}
		
		String name = request.get("name");
		
		try {
			User user = accountService.saveUser(name, username, email);
			
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error occured", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateProfile(@RequestBody HashMap<String, String> request) {
		String id = request.get("id");
		User user = accountService.findById(Integer.parseInt(id));
		if (user == null) {
			return new ResponseEntity<>("userNotFound", HttpStatus.NOT_FOUND);
		}
		try {
			accountService.updateUser(user, request);
			userImageId = user.getId();
			
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error occured", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/photo/upload")
	public ResponseEntity<String> fileUpload(@RequestParam("image") MultipartFile multipartFile) {
		try {
			accountService.saveUserImage(multipartFile, userImageId);
			
			return new ResponseEntity<>("User Picture Saved!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("User Picture Not Saved", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody HashMap<String, String> request) {
		String username = request.get("username");
		User user = accountService.findByUsername(username);
		
		if (user == null) {
			return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
		}
		
		String currentPassword = request.get("currentPassword");
		String newPassword = request.get("newPassword");
		String confirmPassword = request.get("confirmPassword");
		
		if (!newPassword.equals(confirmPassword)) {
			return new ResponseEntity<>("PasswordNotMatched", HttpStatus.BAD_REQUEST);
		}
		
		String userPassword = user.getPassword();
		try {
			if (newPassword != null && !newPassword.isEmpty() && !StringUtils.isEmpty(newPassword)) {
				if (encoder.matches(currentPassword, userPassword)) {
					accountService.updateUserPassword(user, newPassword);
				}
			} else {
				return new ResponseEntity<>("IncorrectCurrentPassword", HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<>("Password Changed Successfully!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error Occured: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/resetPassword/{email}")
	public ResponseEntity<String> resetPassword(@PathVariable("email") String email) {
		User user = accountService.findByEmail(email);
		
		if (user == null) {
			return new ResponseEntity<String>("emailNotFound", HttpStatus.BAD_REQUEST);
		}
		
		accountService.resetPassword(user);
		
		return new ResponseEntity<String>("Email Sent!", HttpStatus.OK);
	}

}
