package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogAveragesResponseDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;
import com.noom.interview.fullstack.sleep.exception.DuplicateSleepLogException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.model.SleepStatus;
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.service.SleepLogService;
import com.noom.interview.fullstack.sleep.util.SleepUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public List<SleepLogResponseDto> getLastNights(UUID userId, LocalDate startDate, LocalDate endDate) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return sleepLogRepository.findByUserIdAndSleepDateBetween(userId, startDate, endDate).stream()
                .map(SleepLogResponseDto::from)
                .collect(Collectors.toList());
    }

    /*
     * corner cases for getAverages:
     * - bedtime crossing midnight (e.g. 23:00 -> 01:00)
     * - average bedtime lands exactly on midnight (00:00)
     *    → e.g. bed times 23:00 and 01:00 -> average = 00:00
     * */
    @Override
    public SleepLogAveragesResponseDto getAverages(UUID userId, LocalDate startDate, LocalDate endDate) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<SleepLog> logs = sleepLogRepository.findByUserIdAndSleepDateBetween(userId, startDate, endDate);

        if (logs.isEmpty()) {
            return new SleepLogAveragesResponseDto(startDate, endDate, 0, null, null, 0, 0, 0);
        }

        int averageMinutesInBed = (int) Math.round(
                logs.stream().mapToInt(SleepLog::getMinutesInBed).average().orElse(0));

        LocalTime averageBedTime = SleepUtil.averageBedTime(
                logs.stream().map(SleepLog::getBedTime).collect(Collectors.toList()));

        LocalTime averageWakeTime = SleepUtil.averageWakeTime(
                logs.stream().map(SleepLog::getWakeTime).collect(Collectors.toList()));

        int badCount = (int) logs.stream().filter(l -> l.getSleepStatus() == SleepStatus.BAD).count();
        int okCount = (int) logs.stream().filter(l -> l.getSleepStatus() == SleepStatus.OK).count();
        int goodCount = (int) logs.stream().filter(l -> l.getSleepStatus() == SleepStatus.GOOD).count();

        return new SleepLogAveragesResponseDto(startDate, endDate, averageMinutesInBed,
                averageBedTime, averageWakeTime, badCount, okCount, goodCount);
    }
}
