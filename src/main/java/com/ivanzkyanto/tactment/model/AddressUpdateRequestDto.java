package com.ivanzkyanto.tactment.model;

import com.ivanzkyanto.tactment.model.request.AddressUpdateRequest;

public record AddressUpdateRequestDto(String contactId, String addressId, AddressUpdateRequest body) {
}
