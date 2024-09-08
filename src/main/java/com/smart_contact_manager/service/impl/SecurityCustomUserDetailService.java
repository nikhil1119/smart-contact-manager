package com.smart_contact_manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.repository.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
		if(user!=null){
			return userRepo.findByEmail(username);
		}
		else throw new  UsernameNotFoundException("User not found with this email id : "+username);
	}

}
