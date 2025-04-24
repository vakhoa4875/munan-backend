package com.rhed.munan.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String ERROR_CODE = "FORBIDDEN";

    public ForbiddenException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}