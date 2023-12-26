package com.ivanzkyanto.tactment.controller;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.annotation.Auth;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import com.ivanzkyanto.tactment.service.ContactService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContactController {

    @NonNull
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<ContactResponse> create(@Auth User user, @RequestBody ContactCreateRequest request) {
        ContactResponse response = contactService.create(user, request);
        return ApiResponse.<ContactResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<ContactResponse> update(
            @Auth User user,
            @PathVariable("contactId") String contactId,
            @RequestBody ContactUpdateRequest request
    ) {
        ContactResponse response = contactService.update(user, contactId, request);
        return ApiResponse.<ContactResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<ContactResponse> get(
            @Auth User user,
            @PathVariable("contactId") String contactId
    ) {
        ContactResponse response = contactService.get(user, contactId);
        return ApiResponse.<ContactResponse>builder().data(response).build();
    }

}
