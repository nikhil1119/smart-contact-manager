package com.smart_contact_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.forms.UserForm;
import com.smart_contact_manager.helper.Message;
import com.smart_contact_manager.helper.MessageType;
import com.smart_contact_manager.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class HomeController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	

	@GetMapping("/")
	public String index() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/about")
	public String about() {
		return "about";
	}

	@RequestMapping("/services")
	public String services() {
		return "services";
	}


	@RequestMapping("/contact")
	public String contact() {
		return "contact";
	}

	//this is showing login page
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	//registration page
	@RequestMapping("/register")
	public String register(Model model) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return "register";
	}

	//processing register
	@RequestMapping(value="/do-register", method=RequestMethod.POST)
	public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session){
		//fetch data

		//validate form data
		if(bindingResult.hasErrors()){
			return "register";
		}

		//save into database
		// User user = User.builder()
		// .name(userForm.getName())
		// .email(userForm.getEmail())
		// .password(userForm.getPassword())
		// .about(userForm.getAbout())
		// .phoneNumber(userForm.getPhoneNumber())
		// .profilePic("/Users/nikhilgadiwadd/SpringBoot/smart-contact-manager/src/main/resources/static/images/profile.jpg")
		// .build();

		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setAbout(userForm.getAbout());
		user.setPhoneNumber(userForm.getPhoneNumber());


		User savedUser = userServiceImpl.saveUser(user);
		//message=Registration successful
		Message message = Message.builder()
		.content("Registration Successful")
		.type(MessageType.green)
		.build();
		session.setAttribute("message", message);
		//redirect
		return "redirect:/register";
	}
	
}
