package com.tenpearls.contactmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z .'-]*$",
            message = "First name contains invalid characters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z .'-]*$",
            message = "Last name contains invalid characters"
    )
    private String lastName;

    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Email type is required")
    @Pattern(
            regexp = "^(Personal|Work|Other)$",
            message = "Email type must be Personal, Work, or Other"
    )
    private String emailType;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9][0-9\\s()\\-]{6,19}$",
            message = "Please enter a valid phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "Phone type is required")
    @Pattern(
            regexp = "^(Personal|Work|Home|Other)$",
            message = "Phone type must be Personal, Work, Home, or Other"
    )
    private String phoneType;

    private Boolean favorite;
}