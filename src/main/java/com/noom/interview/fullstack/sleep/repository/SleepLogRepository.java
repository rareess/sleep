package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.SleepLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SleepLogRepository {

    SleepLog save(SleepLog sleepLog);

    List<SleepLog> findLastNightsByUserId(UUID userId, int nights);

    Optional<SleepLog> findByUserIdAndSleepDate(UUID userId, LocalDate sleepDate);
}
