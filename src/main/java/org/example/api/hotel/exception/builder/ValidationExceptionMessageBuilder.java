package org.example.api.hotel.exception.builder;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidationExceptionMessageBuilder {
    public String buildValidationErrorMessage(List<FieldError> fieldErrors) {
        return "Validation failed for fields: " +
                fieldErrors.stream()
                        .map(fieldError -> fieldError.getField() + " (" + fieldError.getDefaultMessage() + ")")
                        .collect(Collectors.joining(", "));
    }
}