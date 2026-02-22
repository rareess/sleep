package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;
import com.noom.interview.fullstack.sleep.exception.DuplicateSleepLogException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.service.SleepLogService;
import com.noom.interview.fullstack.sleep.util.SleepUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SleepLogServiceImpl implements SleepLogService {

    private final SleepLogRepository sleepLogRepository;
    private final UserRepository userRepository;

    public SleepLogServiceImpl(SleepLogRepository sleepLogRepository, UserRepository userRepository) {
        this.sleepLogRepository = sleepLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SleepLogResponseDto create(UUID userId, CreateSleepLogRequestDto dto) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        sleepLogRepository.findByUserIdAndSleepDate(userId, dto.getSleepDate()).ifPresent(existing -> {
            throw new DuplicateSleepLogException(userId, dto.getSleepDate());
        });

        int minutesInBed = SleepUtil.calculateMinutesInBed(dto.getBedTime(), dto.getWakeTime());

        SleepLog sleepLog = new SleepLog(null, userId, dto.getSleepDate(), dto.getBedTime(),
                dto.getWakeTime(), minutesInBed, dto.getSleepStatus(), null, null);

        return SleepLogResponseDto.from(sleepLogRepository.save(sleepLog));
    }

    @Override
    public List<SleepLogResponseDto> getLastNights(UUID userId, int nights) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return sleepLogRepository.findLastNightsByUserId(userId, nights).stream()
                .map(SleepLogResponseDto::from)
                .collect(Collectors.toList());
    }
}
