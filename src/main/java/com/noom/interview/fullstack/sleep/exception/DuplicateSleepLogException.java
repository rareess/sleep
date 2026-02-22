package com.noom.interview.fullstack.sleep.exception;

import java.time.LocalDate;
import java.util.UUID;

public class DuplicateSleepLogException extends RuntimeException {

    public DuplicateSleepLogException(UUID userId, LocalDate date) {
        super(String.format("Sleep log already exists for user %s on %s", userId, date));
    }
}
