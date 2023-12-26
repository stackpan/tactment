package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ContactResponse;

public interface ContactService {

    ContactResponse create(User user, ContactCreateRequest request);

    ContactResponse update(User user, String contactId, ContactUpdateRequest request);

    ContactResponse get(User user, String contactId);

    void delete(User user, String contactId);

}
