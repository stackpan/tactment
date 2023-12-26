package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.response.ContactResponse;

public interface ContactService {

    ContactResponse create(User user, ContactCreateRequest request);

}
