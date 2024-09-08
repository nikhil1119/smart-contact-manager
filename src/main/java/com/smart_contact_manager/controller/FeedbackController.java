package com.smart_contact_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FeedbackController {

    @Autowired
    private JavaMailSender mailSender;

	@GetMapping("/feedback")
    public String showFeedbackForm() {
        return "user/feedback"; // Return the name of your feedback form view (HTML page)
    }
    @PostMapping("/feedback")
    public String sendFeedback(@RequestParam("name") String name , 
                               @RequestParam("email") String email, 
                               @RequestParam("message") String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("nikhilgadiwadd.work@gmail.com");
            mailMessage.setSubject("New Feedback from " + name);
            mailMessage.setText("Name: " + name + "\n\nEmail: " + email + "\n\nMessage:\n" + message);
			mailSender.send(mailMessage);

            return "redirect:/feedback?success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/feedback?error";
        }
    }

    @GetMapping("/contactMe")
    public String contactMeForm() {
        return "contact"; // Return the name of your feedback form view (HTML page)
    }

    @PostMapping("/contactMe")
    public String contactMe(@RequestParam("name") String name , 
                               @RequestParam("email") String email, 
                               @RequestParam("message") String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("nikhilgadiwadd.work@gmail.com");
            mailMessage.setSubject("New Feedback from " + name);
            mailMessage.setText("Name: " + name + "\n\nEmail: " + email + "\n\nMessage:\n" + message);
			mailSender.send(mailMessage);

            return "redirect:/contactMe?success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/contactMe?error";
        }
    }

    @GetMapping("/homeContact")
    public String homeContactForm() {
        return "home"; // Return the name of your feedback form view (HTML page)
    }

    @PostMapping("/homeContact")
    public String homeContact(@RequestParam("name") String name , 
                               @RequestParam("email") String email, 
                               @RequestParam("message") String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("nikhilgadiwadd.work@gmail.com");
            mailMessage.setSubject("New Feedback from " + name);
            mailMessage.setText("Name: " + name + "\n\nEmail: " + email + "\n\nMessage:\n" + message);
			mailSender.send(mailMessage);

            return "redirect:/home?success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home?error";
        }
    }
    
}

