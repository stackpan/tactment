package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.entity.Contact;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import com.ivanzkyanto.tactment.repository.ContactRepository;
import com.ivanzkyanto.tactment.service.ContactService;
import com.ivanzkyanto.tactment.service.ValidationService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    @NonNull
    private ContactRepository contactRepository;

    @NonNull
    private ValidationService validationService;

    private final ResponseStatusException CONTACT_NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");

    @Override
    @Transactional
    public ContactResponse create(User user, ContactCreateRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId("contact-" + UUID.randomUUID());
        contact.setUser(user);
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepository.save(contact);

        return ContactResponse.build(contact);
    }

    @Override
    @Transactional
    public ContactResponse update(User user, String contactId, ContactUpdateRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> CONTACT_NOT_FOUND_EXCEPTION);

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepository.save(contact);

        return ContactResponse.build(contact);
    }

    @Override
    public ContactResponse get(User user, String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId).orElseThrow(() -> CONTACT_NOT_FOUND_EXCEPTION);
        return ContactResponse.build(contact);
    }

    @Override
    public void delete(User user, String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> CONTACT_NOT_FOUND_EXCEPTION);

        contactRepository.delete(contact);
    }
}
