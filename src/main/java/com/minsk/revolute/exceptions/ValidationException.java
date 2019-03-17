package com.minsk.revolute.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String invalidValue) {
        super(invalidValue);
    }
}
