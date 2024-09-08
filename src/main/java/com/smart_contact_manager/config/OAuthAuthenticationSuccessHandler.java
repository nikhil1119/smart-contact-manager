package com.smart_contact_manager.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.smart_contact_manager.entities.Providers;
import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.helper.AppConstants;
import com.smart_contact_manager.repository.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

	@Autowired
	private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		logger.info("OAuthAuthenticationSuccessHandler");

		DefaultOAuth2User user =(DefaultOAuth2User)authentication.getPrincipal();
		// logger.info(user.getName());
		// user.getAttributes().forEach((key,value)->{
		// 	logger.info("{} => {}", key, value);
		// });
		// logger.info(user.getAuthorities().toString());


		//save data to database
		@SuppressWarnings("null")
		String email = user.getAttribute("email").toString();
		@SuppressWarnings("null")
		String name = user.getAttribute("name").toString();
		@SuppressWarnings("null")
		String picture = user.getAttribute("picture").toString();

		//create user and save to database
		User user1 = new User();
		user1.setEmail(email);
		user1.setName(name);
		user1.setProfilePic(picture);
		user1.setPassword("password");
		user1.setUserId(UUID.randomUUID().toString());
		user1.setProvider(Providers.GOOGLE);
		user1.setEnabled(true);
		user1.setEmailVerified(true);
		user1.setProviderId(user.getName());
		user1.setRoleList(List.of(AppConstants.ROLE_USER));
		user1.setAbout("This account is created using google.");

		User user2 = userRepo.findByEmail(email);

		if(user2==null){
			userRepo.save(user1);
			logger.info("User saved: "+email);
		}


		// response.sendRedirect("/home");
		new DefaultRedirectStrategy().sendRedirect(request,response,"/user/profile");
    }


}
