package com.smart_contact_manager.helper;


public class ResourceNotFoundException extends RuntimeException{
	public ResourceNotFoundException(){
		super("Resource not found");
	}
	public ResourceNotFoundException(String message){
		super(message);
	}
}
