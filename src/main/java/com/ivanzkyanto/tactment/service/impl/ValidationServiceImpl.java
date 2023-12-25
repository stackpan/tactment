package com.ivanzkyanto.tactment.service.impl;

import com.ivanzkyanto.tactment.model.request.Request;
import com.ivanzkyanto.tactment.service.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private Validator validator;

    @Override
    public void validate(Request request) {
        Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
