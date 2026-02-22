package com.noom.interview.fullstack.sleep.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleepLog {

    private UUID id;
    private UUID userId;
    private LocalDate sleepDate;
    private LocalTime bedTime;
    private LocalTime wakeTime;
    private int minutesInBed;
    private SleepStatus sleepStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
