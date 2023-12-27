package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.AddressUpdateRequestDto;
import com.ivanzkyanto.tactment.model.request.AddressCreateRequest;
import com.ivanzkyanto.tactment.model.request.AddressUpdateRequest;
import com.ivanzkyanto.tactment.model.response.AddressResponse;

public interface AddressService {

    AddressResponse create(User user, String contactId, AddressCreateRequest request);

    AddressResponse update(User user, AddressUpdateRequestDto request);

}
