package com.bci.exercise.config.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 731462130627123184L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
