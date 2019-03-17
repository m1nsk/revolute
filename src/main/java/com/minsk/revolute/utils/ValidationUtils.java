package com.minsk.revolute.utils;

import com.minsk.revolute.exceptions.ValidationException;

public class ValidationUtils {
    public static <T> T exceptionIfNull(T value, String message) {
        if(value == null) {
            throw new ValidationException(message);
        }
        return value;
    }
}
