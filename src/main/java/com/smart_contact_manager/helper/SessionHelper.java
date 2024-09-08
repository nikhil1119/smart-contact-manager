package com.smart_contact_manager.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
	public void removeMessage(){
		try {
			System.out.println("Removing message");
			HttpSession session = ((ServletRequestAttributes) (ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
		} catch (Exception e) {
			System.out.println("Error in SessionHelper: "+e);
			e.printStackTrace();
		}
	}
}
