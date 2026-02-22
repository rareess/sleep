package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogAveragesResponseDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
/*
    getLastNights, getAverages
    22/02/2026 - changed Sleep log service to ask for start date and end date instead of number of days.
    this solution handling interval queries could be easy to use in the future for next features.
 */
public interface SleepLogService {

    SleepLogResponseDto create(UUID userId, CreateSleepLogRequestDto dto);

    List<SleepLogResponseDto> getLastNights(UUID userId, LocalDate startDate, LocalDate endDate);

    SleepLogAveragesResponseDto getAverages(UUID userId, LocalDate startDate, LocalDate endDate);
}
