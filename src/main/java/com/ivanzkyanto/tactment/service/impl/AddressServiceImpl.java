package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.entity.Address;
import com.ivanzkyanto.tactment.entity.Contact;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.AddressCreateRequest;
import com.ivanzkyanto.tactment.model.response.AddressResponse;
import com.ivanzkyanto.tactment.repository.AddressRepository;
import com.ivanzkyanto.tactment.repository.ContactRepository;
import com.ivanzkyanto.tactment.service.AddressService;
import com.ivanzkyanto.tactment.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    @NonNull
    private AddressRepository addressRepository;

    @NonNull
    private ValidationService validationService;

    @NonNull
    private ContactRepository contactRepository;

    @Override
    @Transactional
    public AddressResponse create(User user, String contactId, AddressCreateRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setId("address-" + UUID.randomUUID());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepository.save(address);

        return AddressResponse.build(address);
    }
}
