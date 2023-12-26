package com.ivanzkyanto.tactment.controller;

import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.PagingData;
import com.ivanzkyanto.tactment.model.annotation.Auth;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactSearchRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import com.ivanzkyanto.tactment.model.response.PagingResponse;
import com.ivanzkyanto.tactment.service.ContactService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseStatus(HttpStatus.CREATED)
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

    @GetMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ContactResponse> get(@Auth User user, @PathVariable("contactId") String contactId) {
        ContactResponse response = contactService.get(user, contactId);
        return ApiResponse.<ContactResponse>builder().data(response).build();
    }

    @GetMapping(path = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<List<ContactResponse>> search(
            @Auth User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        ContactSearchRequest request = ContactSearchRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .page(page)
                .size(size).build();

        Page<ContactResponse> response = contactService.search(user, request);
        PagingData pagingData = PagingData.builder()
                .currentPage(response.getNumber())
                .size(response.getSize())
                .totalPage(response.getTotalPages())
                .build();

        return PagingResponse.<List<ContactResponse>>builder()
                .data(response.getContent())
                .paging(pagingData)
                .build();
    }

    @DeleteMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> delete(@Auth User user, @PathVariable("contactId") String contactId) {
        contactService.delete(user, contactId);
        return ApiResponse.<String>builder().data("Ok").build();
    }

}
