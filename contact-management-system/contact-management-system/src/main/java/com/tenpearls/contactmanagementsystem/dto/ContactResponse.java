package com.tenpearls.contactmanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String title;

    private String email;

    private String emailType;

    private String phoneNumber;

    private String phoneType;

    private Boolean favorite;
}