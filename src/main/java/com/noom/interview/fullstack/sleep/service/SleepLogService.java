package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;

import java.util.List;
import java.util.UUID;

public interface SleepLogService {

    SleepLogResponseDto create(UUID userId, CreateSleepLogRequestDto dto);

    List<SleepLogResponseDto> getLastNights(UUID userId, int nights);
}
