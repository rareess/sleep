package com.noom.interview.fullstack.sleep.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID id) {
        super(String.format("User not found with id: %s", id));
    }
}
