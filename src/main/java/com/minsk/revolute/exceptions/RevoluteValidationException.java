package com.minsk.revolute.exceptions;

public class RevoluteValidationException extends RuntimeException {
    public RevoluteValidationException(String invalidValue) {
        super(invalidValue);
    }
}
