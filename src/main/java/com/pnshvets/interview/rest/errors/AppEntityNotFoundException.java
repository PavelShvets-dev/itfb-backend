package com.pnshvets.interview.rest.errors;

public class AppEntityNotFoundException extends RuntimeException {
    public AppEntityNotFoundException(String message) {
        super(message);
    }
}
