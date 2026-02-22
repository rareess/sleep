package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.model.SleepStatus;
import com.noom.interview.fullstack.sleep.util.TestSleepLogFactory;
import com.noom.interview.fullstack.sleep.util.TestUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SleepLogRepositoryTest {

    @Autowired
    private SleepLogRepository sleepLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID userId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM sleep_logs");
        jdbcTemplate.execute("DELETE FROM users");

        userId = userRepository.save(TestUserFactory.buildUser("rares", "rares@example.com")).getId();
    }

    @Test
    void save() {
        SleepLog saved = sleepLogRepository.save(TestSleepLogFactory.buildSleepLog(userId, LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getSleepDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(saved.getMinutesInBed()).isEqualTo(480);
        assertThat(saved.getSleepStatus()).isEqualTo(SleepStatus.OK);
    }

    @Test
    void findByUserIdAndSleepDateBetween() {
        sleepLogRepository.save(TestSleepLogFactory.buildSleepLog(userId, LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));
        sleepLogRepository.save(TestSleepLogFactory.buildSleepLog(userId, LocalDate.now().minusDays(2), LocalTime.of(23, 0), LocalTime.of(7, 0)));

        List<SleepLog> result = sleepLogRepository.findByUserIdAndSleepDateBetween(
                userId,
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SleepLog::getSleepDate)
                .containsExactly(LocalDate.now().minusDays(1), LocalDate.now().minusDays(2));
    }

    @Test
    void findByUserIdAndSleepDateBetweenExclude() {
        sleepLogRepository.save(TestSleepLogFactory.buildSleepLog(userId, LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));
        sleepLogRepository.save(TestSleepLogFactory.buildSleepLog(userId, LocalDate.now().minusDays(10), LocalTime.of(22, 0), LocalTime.of(6, 0)));

        List<SleepLog> result = sleepLogRepository.findByUserIdAndSleepDateBetween(
                userId,
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSleepDate()).isEqualTo(LocalDate.now().minusDays(1));
    }

    @Test
    void findByUserIdAndSleepDateShouldReturnLog() {
        LocalDate date = LocalDate.now().minusDays(1);
        sleepLogRepository.save(TestSleepLogFactory.buildSleepLog(userId, date, LocalTime.of(22, 0), LocalTime.of(6, 0)));

        Optional<SleepLog> result = sleepLogRepository.findByUserIdAndSleepDate(userId, date);

        assertThat(result).isPresent();
        assertThat(result.get().getSleepDate()).isEqualTo(date);
        assertThat(result.get().getUserId()).isEqualTo(userId);
    }

    @Test
    void findByUserIdAndSleepDateShouldReturnEmptyWhenNotFound() {
        Optional<SleepLog> result = sleepLogRepository.findByUserIdAndSleepDate(userId, LocalDate.now().minusDays(1));

        assertThat(result).isEmpty();
    }

    @Test
    void findByUserIdAndSleepDateBetweenShouldReturnEmptyWhenNoLogsInRange() {
        List<SleepLog> result = sleepLogRepository.findByUserIdAndSleepDateBetween(
                userId,
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );

        assertThat(result).isEmpty();
    }
}
