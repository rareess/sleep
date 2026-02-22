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
        String sql = "INSERT INTO sleep_logs (user_id, sleep_date, bed_time, wake_time, minutes_in_bed, sleep_status) " +
                     "VALUES (:userId, :sleepDate, :bedTime, :wakeTime, :minutesInBed, :sleepStatus) RETURNING id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", sleepLog.getUserId())
                .addValue("sleepDate", sleepLog.getSleepDate())
                .addValue("bedTime", sleepLog.getBedTime())
                .addValue("wakeTime", sleepLog.getWakeTime())
                .addValue("minutesInBed", sleepLog.getMinutesInBed())
                .addValue("sleepStatus", sleepLog.getSleepStatus().name());

        UUID generatedId = jdbcTemplate.queryForObject(sql, params, UUID.class);
        sleepLog.setId(generatedId);
        log.info("SleepLog saved with generated id {}", sleepLog.getId());
        return sleepLog;
    }

    @Override
    public List<SleepLog> findLastNightsByUserId(UUID userId, int nights) {
        String sql = "SELECT id, user_id, sleep_date, bed_time, wake_time, minutes_in_bed, sleep_status FROM sleep_logs " +
                     "WHERE user_id = :userId " +
                     "AND sleep_date BETWEEN CURRENT_DATE - :nights AND CURRENT_DATE - 1 " +
                     "ORDER BY sleep_date DESC";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("nights", nights);

        List<SleepLog> sleepLogs = jdbcTemplate.query(sql, params, sleepLogRowMapper);
        log.info("Found {} sleep logs for user {} last nights : {}", sleepLogs.size(), userId, nights);
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
