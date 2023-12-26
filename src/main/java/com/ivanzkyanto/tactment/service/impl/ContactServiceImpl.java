package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.entity.Contact;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactSearchRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import com.ivanzkyanto.tactment.repository.ContactRepository;
import com.ivanzkyanto.tactment.service.ContactService;
import com.ivanzkyanto.tactment.service.ValidationService;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, ContactSearchRequest request) {
        validationService.validate(request);

        Specification<Contact> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user"), user));

            if (Objects.nonNull(request.getName())) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstName"), "%" + request.getName() + "%"),
                        criteriaBuilder.like(root.get("lastName"), "%" + request.getName() + "%")
                ));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }

            if (Objects.nonNull(request.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(ContactResponse::build)
                .toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

    @Override
    @Transactional
    public void delete(User user, String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> CONTACT_NOT_FOUND_EXCEPTION);

        contactRepository.delete(contact);
    }
}
