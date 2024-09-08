package com.smart_contact_manager.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserForm {

	@NotBlank(message="name is required")
	@Size(min=3, message="Minimum 3 characters are required")
	private String name;
	@Email(message="Invalid email")
	@NotBlank(message="email is required")
	private String email;
	@Size(min=6, message="Password should contain at least 6 characters")
	@NotBlank(message="Password is required")
	private String password;
	@NotBlank(message="About is required")
	private String about;
	@Size(min=8, max=12, message="Invlaid phone number")
	@Pattern(regexp="(^$|[0-9]{10})",message="Only numeric values are allowed")
	private String phoneNumber;
}
