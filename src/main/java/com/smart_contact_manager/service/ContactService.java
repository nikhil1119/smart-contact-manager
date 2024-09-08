package com.smart_contact_manager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.smart_contact_manager.entities.Contact;
import com.smart_contact_manager.entities.User;

public interface ContactService {

	//save contact
	Contact save(Contact contact);

	//update contact
	Contact update(Contact contact);

	//get all contacts
	List<Contact> getAll();
	
	//get contact by id
	Contact getById(String id);

	//delete contact
	void delete(String id);

	//search contact
	Page<Contact> searchByName(String nameKeyword, int page, int size, String sortField, String sortDirection, User user);
	Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortField, String sortDirection, User user);
	Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortField, String sortDirection, User user);

	//get contacts by userid
	List<Contact> getByUserId(String userId);

	//get contacts by user
	Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

}
