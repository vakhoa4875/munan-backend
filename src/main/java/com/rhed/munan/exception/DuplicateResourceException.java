package com.rhed.munan.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String ERROR_CODE = "DUPLICATE_RESOURCE";

    public DuplicateResourceException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue), STATUS, ERROR_CODE);
    }
}