package com.smart_contact_manager.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {
	static Logger logger = LoggerFactory.getLogger(Helper.class);

	public static String getEmailOfLoggedInUser(Authentication authentication){

		// AuthenticationPrincipal principal = (AuthenticationPrincipal) authentication.getPrincipal();
		
		if(authentication instanceof OAuth2AuthenticationToken){
			//sign in with google
			var user =(DefaultOAuth2User)authentication.getPrincipal();
			String email = user.getAttribute("email").toString();
			return email;
		}
		else{
			//sign in with email
			logger.info("inside email");
			return authentication.getName();
		}
	}
}
