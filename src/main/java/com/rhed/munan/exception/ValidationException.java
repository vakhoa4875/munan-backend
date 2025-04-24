package com.rhed.munan.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String ERROR_CODE = "VALIDATION_ERROR";

    private final Map<String, String> errors;

    public ValidationException(String message, Map<String, String> errors) {
        super(message, STATUS, ERROR_CODE);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}