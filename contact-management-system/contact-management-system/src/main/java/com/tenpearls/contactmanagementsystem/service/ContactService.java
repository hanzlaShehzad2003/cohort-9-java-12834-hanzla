package com.tenpearls.contactmanagementsystem.service;

import com.tenpearls.contactmanagementsystem.dto.ContactRequest;
import com.tenpearls.contactmanagementsystem.dto.ContactResponse;
import com.tenpearls.contactmanagementsystem.entity.Contact;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.exception.ContactNotFoundException;
import com.tenpearls.contactmanagementsystem.exception.UserNotFoundException;
import com.tenpearls.contactmanagementsystem.repository.ContactRepository;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private static final Logger logger =
            LoggerFactory.getLogger(ContactService.class);

    private static final String CONTACT_NOT_FOUND =
            "Contact not found";

    private static final String USER_NOT_FOUND =
            "User not found";

    private final ContactRepository contactRepository;

    private final UserRepository userRepository;


    public ContactResponse createContact(
            Long userId,
            ContactRequest request) {

        logger.info(
                "Creating contact for user ID {}",
                userId
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(USER_NOT_FOUND)
                );

        Contact contact = Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .title(request.getTitle())
                .email(request.getEmail())
                .emailType(request.getEmailType())
                .phoneNumber(request.getPhoneNumber())
                .phoneType(request.getPhoneType())
                .favorite(Boolean.TRUE.equals(request.getFavorite()))
                .user(user)
                .build();

        contact = contactRepository.save(contact);

        logger.info(
                "Contact saved successfully with ID {}",
                contact.getId()
        );

        return mapToResponse(contact);
    }


    public List<ContactResponse> getAllContacts(
            Long userId,
            int page,
            int size,
            String sortBy) {

        logger.info(
                "Fetching contacts for user ID {}, page {}, size {}",
                userId,
                page,
                size
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        return contactRepository
                .findByUserId(userId, pageable)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public ContactResponse getContactById(
            Long userId,
            Long contactId) {

        logger.info(
                "Fetching contact ID {} for user ID {}",
                contactId,
                userId
        );

        Contact contact = contactRepository
                .findByIdAndUserId(contactId, userId)
                .orElseThrow(() ->
                        new ContactNotFoundException(
                                CONTACT_NOT_FOUND
                        )
                );

        return mapToResponse(contact);
    }


    public ContactResponse updateContact(
            Long userId,
            Long contactId,
            ContactRequest request) {

        logger.info(
                "Updating contact ID {} for user ID {}",
                contactId,
                userId
        );

        Contact contact = contactRepository
                .findByIdAndUserId(contactId, userId)
                .orElseThrow(() ->
                        new ContactNotFoundException(
                                CONTACT_NOT_FOUND
                        )
                );

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setTitle(request.getTitle());
        contact.setEmail(request.getEmail());
        contact.setEmailType(request.getEmailType());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setPhoneType(request.getPhoneType());

        if (request.getFavorite() != null) {
            contact.setFavorite(request.getFavorite());
        }

        contact = contactRepository.save(contact);

        logger.info(
                "Contact ID {} updated successfully",
                contactId
        );

        return mapToResponse(contact);
    }


    public void deleteContact(
            Long userId,
            Long contactId) {

        logger.info(
                "Deleting contact ID {} for user ID {}",
                contactId,
                userId
        );

        Contact contact = contactRepository
                .findByIdAndUserId(contactId, userId)
                .orElseThrow(() ->
                        new ContactNotFoundException(
                                CONTACT_NOT_FOUND
                        )
                );

        contactRepository.delete(contact);

        logger.info(
                "Contact ID {} deleted successfully",
                contactId
        );
    }


    public ContactResponse toggleFavorite(
            Long userId,
            Long contactId) {

        logger.info(
                "Toggling favorite for contact ID {} and user ID {}",
                contactId,
                userId
        );

        Contact contact = contactRepository
                .findByIdAndUserId(contactId, userId)
                .orElseThrow(() ->
                        new ContactNotFoundException(
                                CONTACT_NOT_FOUND
                        )
                );

        contact.setFavorite(!contact.getFavorite());

        contact = contactRepository.save(contact);

        logger.info(
                "Favorite status updated successfully"
        );

        return mapToResponse(contact);
    }


    public List<ContactResponse> searchContacts(
            Long userId,
            String keyword) {

        logger.info(
                "Searching contacts for user ID {} with query length {}",
                userId,
                keyword.length()
        );

        return contactRepository
                .searchContacts(userId, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private ContactResponse mapToResponse(
            Contact contact) {

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .title(contact.getTitle())
                .email(contact.getEmail())
                .emailType(contact.getEmailType())
                .phoneNumber(contact.getPhoneNumber())
                .phoneType(contact.getPhoneType())
                .favorite(contact.getFavorite())
                .build();
    }
}