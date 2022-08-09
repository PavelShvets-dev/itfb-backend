package com.pnshvets.interview.rest.errors;

public class AppEntityAlreadyExistsException extends RuntimeException {
    public AppEntityAlreadyExistsException(String message) {
        super(message);
    }
}
