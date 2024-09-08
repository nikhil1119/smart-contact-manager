package com.smart_contact_manager.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.smart_contact_manager.helper.AppConstants;
import com.smart_contact_manager.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

	@Autowired
	private Cloudinary cloudinary;


    @Override
	public String uploadImage(MultipartFile contactImage, String filename) {

		//code to upload image to cloud
		try {
			byte[] data = new byte[contactImage.getInputStream().available()];
			contactImage.getInputStream().read(data);
			cloudinary.uploader().upload(data, ObjectUtils.asMap(
				"public_id",filename
			));
			return this.getUrlFromPublicId(filename);
		} catch (IOException e) {
			System.out.println("Image Upload failed");
			e.printStackTrace();
			return null;
		}
		//returns url
	}

	@Override
	public String getUrlFromPublicId(String publicId) {
		return cloudinary
		.url()
		.transformation(
			new Transformation<>()
			.width(AppConstants.CONTACT_IMAGE_WIDTH)
			.height(AppConstants.CONTACT_IMAGE_HEIGHT)
			.crop(AppConstants.CONTACT_IMAGE_CROP)
			)
		.generate(publicId);
	}
	

}
