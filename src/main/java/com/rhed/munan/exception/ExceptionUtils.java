package com.rhed.munan.exception;

import java.util.Map;
import java.util.function.Supplier;

public class ExceptionUtils {

    public static ResourceNotFoundException notFound(String resourceName, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(resourceName, fieldName, fieldValue);
    }

    public static <T> T orElseThrow(T resource, String resourceName, String fieldName, Object fieldValue) {
        if (resource == null) {
            throw notFound(resourceName, fieldName, fieldValue);
        }
        return resource;
    }

    public static <T> Supplier<ResourceNotFoundException> notFoundSupplier(String resourceName, String fieldName, Object fieldValue) {
        return () -> notFound(resourceName, fieldName, fieldValue);
    }

    public static DuplicateResourceException duplicate(String resourceName, String fieldName, Object fieldValue) {
        return new DuplicateResourceException(resourceName, fieldName, fieldValue);
    }

    public static BadRequestException badRequest(String message) {
        return new BadRequestException(message);
    }

    public static UnauthorizedException unauthorized(String message) {
        return new UnauthorizedException(message);
    }

    public static ForbiddenException forbidden(String message) {
        return new ForbiddenException(message);
    }

    public static ValidationException validation(String message, Map<String, String> errors) {
        return new ValidationException(message, errors);
    }

    public static ServiceException serviceError(String message) {
        return new ServiceException(message);
    }

    public static ServiceException serviceError(String message, Throwable cause) {
        return new ServiceException(message, cause);
    }
}