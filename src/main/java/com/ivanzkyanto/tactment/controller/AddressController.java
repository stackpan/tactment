package com.ivanzkyanto.tactment.controller;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.AddressUpdateRequestDto;
import com.ivanzkyanto.tactment.model.annotation.Auth;
import com.ivanzkyanto.tactment.model.request.AddressCreateRequest;
import com.ivanzkyanto.tactment.model.request.AddressUpdateRequest;
import com.ivanzkyanto.tactment.model.response.AddressResponse;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.service.AddressService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AddressController {

    @NonNull
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddressResponse> create(
            @Auth User user,
            @PathVariable("contactId") String contactId,
            @RequestBody AddressCreateRequest request) {
        AddressResponse response = addressService.create(user, contactId, request);
        return ApiResponse.<AddressResponse>builder()
                .data(response)
                .build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<AddressResponse> update(
            @Auth User user,
            @PathVariable("contactId") String contactId,
            @PathVariable("addressId") String addressId,
            @RequestBody AddressUpdateRequest body
    ) {
        AddressUpdateRequestDto request = new AddressUpdateRequestDto(contactId, addressId, body);
        AddressResponse response = addressService.update(user, request);
        
        return ApiResponse.<AddressResponse>builder()
                .data(response)
                .build();
    }

}
