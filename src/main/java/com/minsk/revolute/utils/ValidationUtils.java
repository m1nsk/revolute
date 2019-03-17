package com.minsk.revolute.utils;

import com.minsk.revolute.exceptions.RevoluteValidationException;

public class ValidationUtils {
    public static <T> T exceptionIfNull(T value, String message) {
        if(value == null) {
            throw new RevoluteValidationException(message);
        }
        return value;
    }
}
