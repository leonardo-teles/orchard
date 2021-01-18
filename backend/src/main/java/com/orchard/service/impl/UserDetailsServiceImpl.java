package com.orchard.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.orchard.model.User;
import com.orchard.model.UserRole;
import com.orchard.service.AccountService;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountService accountService;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = accountService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + username + " was not found");
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Set<UserRole> userRoles = user.getUserRoles();
		userRoles.forEach(userRole -> {
			authorities.add(new SimpleGrantedAuthority(userRoles.toString()));
		});
		
		//left this just for not changing my model and the entire application. It'll be changing after I have the API finished
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

}
