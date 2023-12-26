package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import jakarta.transaction.Transactional;

public interface ContactService {

    @Transactional
    ContactResponse create(User user, ContactCreateRequest request);

    @Transactional
    ContactResponse update(User user, String contactId, ContactUpdateRequest request);

}
