package com.ivanzkyanto.tactment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AddressNotFoundException extends ResponseStatusException {
    public AddressNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Address not found");
    }
}
