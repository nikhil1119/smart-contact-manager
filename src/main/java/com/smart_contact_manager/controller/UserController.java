package com.smart_contact_manager.controller;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.helper.Helper;
import com.smart_contact_manager.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);


	//user profile
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	public String userProfile(Model model, Authentication authentication, @AuthenticationPrincipal OAuth2User principal) {
		String username = Helper.getEmailOfLoggedInUser(authentication);	

		logger.info("User logged in: {}",username);
		User user = userService.getUserByEmail(username);
		System.out.println(user.getName());
		System.out.println(user.getEmail());
		model.addAttribute("loggedInUser",user);

    // String email = principal.getAttribute("email");
    // String fullName = principal.getAttribute("name");
    // String givenName = principal.getAttribute("given_name");
    // String familyName = principal.getAttribute("family_name");
	String profilePicUrl = principal.getAttribute("picture");
	user.setProfilePic(profilePicUrl);
    // if(user.getProfilePic()==null){
		
	// }
	// else{
	// 	user.setProfilePic(profilePicUrl);
	// }

	

    // model.addAttribute("email", email);
    // model.addAttribute("fullName", fullName);
    // model.addAttribute("givenName", givenName);
    // model.addAttribute("familyName", familyName);
    model.addAttribute("profilePicUrl", profilePicUrl);

		return "user/profile";
	}
	
	@RequestMapping(value="/profile/edit", method=RequestMethod.GET)
	public String editProfile(Model model, Authentication authentication) {
		String username = Helper.getEmailOfLoggedInUser(authentication);	
		User user = userService.getUserByEmail(username);

		model.addAttribute("loggedInUser", user);
		return "user/edit_profile";
	}

	@RequestMapping(value="/profile/edit", method=RequestMethod.POST)
	public String updateProfile(
		@AuthenticationPrincipal OAuth2User principal,
		@RequestParam("name") String name,
		@RequestParam("phoneNumber") String phoneNumber,
		@RequestParam("about") String about,
		@RequestParam(value = "profilePic", required = false) MultipartFile profilePic1,
		RedirectAttributes redirectAttributes
	) {
		String username = principal.getAttribute("email");
		User user = userService.getUserByEmail(username);

		if(name!=null){
			user.setName(name);
		}
		if (phoneNumber!=null) {
			user.setPhoneNumber(phoneNumber);
		}
		user.setAbout(about);

		//change profile picture in update profile menu
		if (profilePic1 != null && !profilePic1.isEmpty()) {
			// Handle file upload
			String fileName = profilePic1.getOriginalFilename();
			String uploadDir = System.getProperty("user.dir") + "/uploads/";
			String filePath = uploadDir + fileName;
			try {
				// File dir = new File(uploadDir);
            	// if (!dir.exists()) dir.mkdirs();
				profilePic1.transferTo(new File(filePath));
				user.setProfilePic(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		userService.updateUser(user);
		redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");

		return "redirect:/user/profile";
	}
	
	
}
