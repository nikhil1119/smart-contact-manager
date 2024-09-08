package com.smart_contact_manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.helper.Helper;
import com.smart_contact_manager.service.UserService;

@ControllerAdvice
public class RootController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@ModelAttribute
	public void addLoggedInUserInformation(Model model, Authentication authentication){
		if(authentication==null){
			return;
		}
		System.out.println("Adding logged in user information to the model");
		String username = Helper.getEmailOfLoggedInUser(authentication);	
		logger.info("User logged in: {}",username);
		//fetch user from db:
		User user = userService.getUserByEmail(username);
		// System.out.println("User: "+user);
		System.out.println(user.getName());
		System.out.println(user.getEmail());
		model.addAttribute("loggedInUser",user);
	}
}
