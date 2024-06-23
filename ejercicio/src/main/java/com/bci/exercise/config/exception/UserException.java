package com.bci.exercise.config.exception;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = 6222023898098739577L;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
