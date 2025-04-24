package com.rhed.munan.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String ERROR_CODE = "SERVICE_ERROR";

    public ServiceException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause, STATUS, ERROR_CODE);
    }
}