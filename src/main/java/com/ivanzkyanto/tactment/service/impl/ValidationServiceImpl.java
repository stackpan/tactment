package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.model.request.Request;
import com.ivanzkyanto.tactment.service.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    @NonNull
    private Validator validator;

    @Override
    public void validate(Request request) {
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
