package com.noom.interview.fullstack.sleep.dto;

import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.model.SleepStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleepLogResponseDto {

    private UUID id;
    private UUID userId;
    private LocalDate sleepDate;
    private LocalTime bedTime;
    private LocalTime wakeTime;
    private int minutesInBed;
    private SleepStatus sleepStatus;

    public static SleepLogResponseDto from(SleepLog sleepLog) {
        return new SleepLogResponseDto(
                sleepLog.getId(),
                sleepLog.getUserId(),
                sleepLog.getSleepDate(),
                sleepLog.getBedTime(),
                sleepLog.getWakeTime(),
                sleepLog.getMinutesInBed(),
                sleepLog.getSleepStatus()
        );
    }
}
