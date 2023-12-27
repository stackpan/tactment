package com.ivanzkyanto.tactment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ContactNotFoundException extends ResponseStatusException {
    public ContactNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Contact not found");
    }
}
