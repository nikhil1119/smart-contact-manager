package com.smart_contact_manager.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	String uploadImage(MultipartFile contactImage, String filename);

	String getUrlFromPublicId(String publicId);

}
