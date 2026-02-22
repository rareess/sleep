package com.noom.interview.fullstack.sleep.repository.impl;

import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository;
import com.noom.interview.fullstack.sleep.repository.mapper.SleepLogRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class SleepLogRepositoryImpl implements SleepLogRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SleepLogRowMapper sleepLogRowMapper;

    public SleepLogRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, SleepLogRowMapper sleepLogRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.sleepLogRowMapper = sleepLogRowMapper;
    }

    @Override
    public SleepLog save(SleepLog sleepLog) {
        sleepLog.setId(UUID.randomUUID());

        String sql = "INSERT INTO sleep_logs (id, user_id, sleep_date, bed_time, wake_time, minutes_in_bed, sleep_status) " +
                     "VALUES (:id, :userId, :sleepDate, :bedTime, :wakeTime, :minutesInBed, :sleepStatus)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", sleepLog.getId())
                .addValue("userId", sleepLog.getUserId())
                .addValue("sleepDate", sleepLog.getSleepDate())
                .addValue("bedTime", sleepLog.getBedTime())
                .addValue("wakeTime", sleepLog.getWakeTime())
                .addValue("minutesInBed", sleepLog.getMinutesInBed())
                .addValue("sleepStatus", sleepLog.getSleepStatus().name());

        jdbcTemplate.update(sql, params);
        log.info("SleepLog saved with id {}", sleepLog.getId());
        return sleepLog;
    }

    @Override
    public List<SleepLog> findByUserIdAndSleepDateBetween(UUID userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT id, user_id, sleep_date, bed_time, wake_time, minutes_in_bed, sleep_status FROM sleep_logs " +
                     "WHERE user_id = :userId " +
                     "AND sleep_date BETWEEN :startDate AND :endDate " +
                     "ORDER BY sleep_date DESC";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);

        List<SleepLog> sleepLogs = jdbcTemplate.query(sql, params, sleepLogRowMapper);
        log.info("Found {} sleep logs for user {} between {} and {}", sleepLogs.size(), userId, startDate, endDate);
        return sleepLogs;
    }

    @Override
    public Optional<SleepLog> findByUserIdAndSleepDate(UUID userId, LocalDate sleepDate) {
        String sql = "SELECT id, user_id, sleep_date, bed_time, wake_time, minutes_in_bed, sleep_status FROM sleep_logs WHERE user_id = :userId AND sleep_date = :sleepDate";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("sleepDate", sleepDate);

        return jdbcTemplate.query(sql, params, sleepLogRowMapper).stream().findFirst();
    }
}
