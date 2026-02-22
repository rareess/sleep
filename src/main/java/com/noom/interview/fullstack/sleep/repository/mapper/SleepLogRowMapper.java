package com.noom.interview.fullstack.sleep.repository.mapper;

import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.model.SleepStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class SleepLogRowMapper implements RowMapper<SleepLog> {

    @Override
    public SleepLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        SleepLog sleepLog = new SleepLog();
        sleepLog.setId((UUID) rs.getObject("id"));
        sleepLog.setUserId((UUID) rs.getObject("user_id"));
        sleepLog.setSleepDate(rs.getDate("sleep_date").toLocalDate());
        sleepLog.setBedTime(rs.getTime("bed_time").toLocalTime());
        sleepLog.setWakeTime(rs.getTime("wake_time").toLocalTime());
        sleepLog.setMinutesInBed(rs.getInt("minutes_in_bed"));
        sleepLog.setSleepStatus(SleepStatus.valueOf(rs.getString("sleep_status")));
        return sleepLog;
    }
}
