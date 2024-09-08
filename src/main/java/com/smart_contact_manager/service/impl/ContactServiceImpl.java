package com.smart_contact_manager.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smart_contact_manager.entities.Contact;
import com.smart_contact_manager.entities.User;
import com.smart_contact_manager.helper.ResourceNotFoundException;
import com.smart_contact_manager.repository.ContactRepo;
import com.smart_contact_manager.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	private ContactRepo contactRepo;

	@Override
	public Contact save(Contact contact) {
		String contactId = UUID.randomUUID().toString();
		contact.setId(contactId);
		return contactRepo.save(contact);
	}

	@Override
	public Contact update(Contact contact) {
		var contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavourite(contact.isFavourite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);
	}

	@Override
	public List<Contact> getAll() {
		return contactRepo.findAll();
	}

	@Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

	@Override
	public void delete(String id) {
		contactRepo.deleteById(id);
	}

	@Override
	public List<Contact> getByUserId(String userId) {
		return contactRepo.findByUserId(userId);
	}

	@Override
	public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		var pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUser(user, pageable);
	}

	@Override
	public Page<Contact> searchByName(String nameKeyword, int page, int size, String sortBy, String direction, User user) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		var pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByNameContainingAndUser(nameKeyword, user, pageable);
	}

	@Override
	public Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortBy,
			String direction, User user) {
				Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
				var pageable = PageRequest.of(page, size, sort);
				return contactRepo.findByEmailContainingAndUser(emailKeyword, user, pageable);
	}

	@Override
	public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortBy, String direction, User user) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
				var pageable = PageRequest.of(page, size, sort);
				return contactRepo.findByPhoneNumberContainingAndUser(phoneNumberKeyword, user, pageable);
	}

}
