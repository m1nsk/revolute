package com.minsk.revolute.exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String invalidAmount) {
        super(invalidAmount);
    }
}
