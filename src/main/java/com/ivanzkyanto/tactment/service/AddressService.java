package com.ivanzkyanto.tactment.service;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.AddressUpdateRequestDto;
import com.ivanzkyanto.tactment.model.request.AddressCreateRequest;
import com.ivanzkyanto.tactment.model.response.AddressResponse;

import java.util.List;

public interface AddressService {

    List<AddressResponse> list(User user, String contactId);

    AddressResponse create(User user, String contactId, AddressCreateRequest request);

    AddressResponse update(User user, AddressUpdateRequestDto request);

    AddressResponse get(User user, String contactId, String addressId);

    void delete(User user, String contactId, String addressId);
}
