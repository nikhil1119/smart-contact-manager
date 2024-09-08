package com.smart_contact_manager.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart_contact_manager.entities.Contact;
import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.forms.ContactForm;
import com.smart_contact_manager.forms.ContactSearchForm;
import com.smart_contact_manager.helper.AppConstants;
import com.smart_contact_manager.helper.Helper;
import com.smart_contact_manager.helper.Message;
import com.smart_contact_manager.helper.MessageType;
import com.smart_contact_manager.service.ImageService;
import com.smart_contact_manager.service.UserService;
import com.smart_contact_manager.service.impl.ContactServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);  

	@Autowired
	private ContactServiceImpl contactService;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;
	
	@RequestMapping("/add")
	public String contact(Model model){
		ContactForm contactForm =  new ContactForm();
		model.addAttribute("contactForm", contactForm);
		return "user/add_contact";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data

        // 1 validate form

        if (result.hasErrors()) {

            result.getAllErrors().forEach(error -> logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        // form ---> contact

        User user = userService.getUserByEmail(username);
        // 2 process the contact picture

        // image process

        // uplod karne ka code
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavourite(contactForm.isFavourite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);

        }
        contactService.save(contact);
        System.out.println(contactForm);

        // 3 set the contact picture url

        // 4 `set message to be displayed on the view

        session.setAttribute("message",
                Message.builder()
                        .content("You have successfully added a new contact")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts/add";

    }
	
	@RequestMapping
	public String viewContacts(
		@RequestParam(value="page", defaultValue="0") int page,
		@RequestParam(value="size",defaultValue=AppConstants.PAGE_SIZE+"") int size,
		@RequestParam(value="sortBy", defaultValue="name") String sortBy,
		@RequestParam(value="direction", defaultValue="asc") String direction,
		Model model, 
		Authentication authentication){
		//load all the user contacts
		String username = Helper.getEmailOfLoggedInUser(authentication);
		User user = userService.getUserByEmail(username);
		Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);
		model.addAttribute("pageContact",pageContact);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
		model.addAttribute("contactSearchForm",new ContactSearchForm());

		return "user/contacts";
	}

	@RequestMapping("/search")
	public String searchHandler(
		@ModelAttribute ContactSearchForm contactSearchForm,
		@RequestParam(value = "size", defaultValue=AppConstants.PAGE_SIZE+"") int size,
		@RequestParam(value="page", defaultValue="0") int page,
		@RequestParam(value="sortBy", defaultValue="name") String sortBy,
		@RequestParam(value="direction", defaultValue="asc") String direction,
		Model model,
		Authentication authentication
	)
	{
		logger.info("field {} keyword {}", contactSearchForm.getField(),contactSearchForm.getKeyword());

		var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

		Page<Contact> pageContact = null;
		if(contactSearchForm.getField().equalsIgnoreCase("name")){
			pageContact = contactService.searchByName(contactSearchForm.getKeyword(), page, size, sortBy, direction, user);
		}
		else if(contactSearchForm.getField().equalsIgnoreCase("email")){
			pageContact = contactService.searchByEmail(contactSearchForm.getKeyword(), page, size, sortBy, direction,user);
		}
		else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
			pageContact = contactService.searchByPhoneNumber(contactSearchForm.getKeyword(), page, size, sortBy, direction,user);
		}
		logger.info("pageContact {}", pageContact);
		model.addAttribute("pageContact",pageContact);
		model.addAttribute("contactSearchForm",contactSearchForm);
		model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
		return "user/search";
	}

	@RequestMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") String contactId){
		contactService.delete(contactId);
		logger.info("ContactId {} deleted.",contactId);
		return "redirect:/user/contacts";
	}


   // update contact form view
   @GetMapping("/view/{contactId}")
   public String updateContactFormView(
		   @PathVariable("contactId") String contactId,
		   Model model) {

	   var contact = contactService.getById(contactId);
	   ContactForm contactForm = new ContactForm();
	   contactForm.setName(contact.getName());
	   contactForm.setEmail(contact.getEmail());
	   contactForm.setPhoneNumber(contact.getPhoneNumber());
	   contactForm.setAddress(contact.getAddress());
	   contactForm.setDescription(contact.getDescription());
	   contactForm.setFavourite(contact.isFavourite());
	   contactForm.setWebsiteLink(contact.getWebsiteLink());
	   contactForm.setLinkedInLink(contact.getLinkedInLink());
	   contactForm.setPicture(contact.getPicture());
	   model.addAttribute("contactForm", contactForm);
	   model.addAttribute("contactId", contactId);

	   return "user/update_contact_view";
   }

   @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
   public String updateContact(@PathVariable("contactId") String contactId,
		   @Valid @ModelAttribute ContactForm contactForm,
		   BindingResult bindingResult,
		   Model model,
		   HttpSession session) {

	   // update the contact
	   if (bindingResult.hasErrors()) {
		   return "user/update_contact_view";
	   }

	   var con = contactService.getById(contactId);
	   con.setId(contactId);
	   con.setName(contactForm.getName());
	   con.setEmail(contactForm.getEmail());
	   con.setPhoneNumber(contactForm.getPhoneNumber());
	   con.setAddress(contactForm.getAddress());
	   con.setDescription(contactForm.getDescription());
	   con.setFavourite(contactForm.isFavourite());
	   con.setWebsiteLink(contactForm.getWebsiteLink());
	   con.setLinkedInLink(contactForm.getLinkedInLink());

	   // process image:

	   if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
		   logger.info("file is not empty");
		   String fileName = UUID.randomUUID().toString();
		   String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
		   con.setCloudinaryImagePublicId(fileName);
		   con.setPicture(imageUrl);
		   contactForm.setPicture(imageUrl);

	   } else {
		   logger.info("file is empty");
	   }

	   var updateCon = contactService.update(con);
	   logger.info("updated contact {}", updateCon);

	   session.setAttribute("message",
                Message.builder()
                        .content("Contact Updated Successfully!!")
                        .type(MessageType.green)
                        .build());

	   return "redirect:/user/contacts/view/" + contactId;
   }

}

