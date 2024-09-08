package com.smart_contact_manager.forms;

import org.springframework.web.multipart.MultipartFile;

import com.smart_contact_manager.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContactForm {
    @NotBlank(message="Name is required")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Invalid email [example@gmail.com]")
    private String email;
    
    @Size(min=8, max=12, message="Invlaid phone number")
	@Pattern(regexp="(^$|[0-9]{10})",message="Only numeric values are allowed")
    private String phoneNumber;

    @NotBlank(message="Address is required")
	private String address;

	private String description;

    private boolean favourite;

    private String websiteLink;

    private String linkedInLink;

    //annotation create karenge jo file validate karega
    //size & resolution
    @ValidFile
    private MultipartFile contactImage;

    private String picture;
}

