package com.noom.interview.fullstack.sleep.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User already exists with the provided credentials");
    }
}
