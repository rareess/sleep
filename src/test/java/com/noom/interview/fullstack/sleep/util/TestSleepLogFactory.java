package com.noom.interview.fullstack.sleep.util;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.model.SleepStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public final class TestSleepLogFactory {

    private TestSleepLogFactory() {}

    public static SleepLog buildSleepLog(UUID userId, LocalDate date, LocalTime bedTime, LocalTime wakeTime) {
        int minutes = SleepUtil.calculateMinutesInBed(bedTime, wakeTime);
        return new SleepLog(null, userId, date, bedTime, wakeTime, minutes, SleepStatus.OK, null, null);
    }

    public static CreateSleepLogRequestDto buildCreateRequest(LocalDate date, LocalTime bedTime, LocalTime wakeTime) {
        CreateSleepLogRequestDto dto = new CreateSleepLogRequestDto();
        dto.setSleepDate(date);
        dto.setBedTime(bedTime);
        dto.setWakeTime(wakeTime);
        dto.setSleepStatus(SleepStatus.OK);
        return dto;
    }
}
