package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactSearchRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import org.springframework.data.domain.Page;

public interface ContactService {

    ContactResponse create(User user, ContactCreateRequest request);

    ContactResponse update(User user, String contactId, ContactUpdateRequest request);

    ContactResponse get(User user, String contactId);

    Page<ContactResponse> search(User user, ContactSearchRequest request);

    void delete(User user, String contactId);

}
