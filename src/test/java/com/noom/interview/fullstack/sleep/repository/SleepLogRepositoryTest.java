package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.SleepLog;
import com.noom.interview.fullstack.sleep.model.SleepStatus;
import com.noom.interview.fullstack.sleep.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

        User user = new User();
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        userId = userRepository.save(user).getId();
    }

    @Test
    void save() {
        SleepLog saved = sleepLogRepository.save(buildEntity(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getSleepDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(saved.getMinutesInBed()).isEqualTo(480);
        assertThat(saved.getSleepStatus()).isEqualTo(SleepStatus.OK);
    }

    @Test
    void findByUserIdAndSleepDateBetween() {
        sleepLogRepository.save(buildEntity(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));
        sleepLogRepository.save(buildEntity(LocalDate.now().minusDays(2), LocalTime.of(23, 0), LocalTime.of(7, 0)));

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
        sleepLogRepository.save(buildEntity(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));
        sleepLogRepository.save(buildEntity(LocalDate.now().minusDays(10), LocalTime.of(22, 0), LocalTime.of(6, 0)));

        List<SleepLog> result = sleepLogRepository.findByUserIdAndSleepDateBetween(
                userId,
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSleepDate()).isEqualTo(LocalDate.now().minusDays(1));
    }

    private SleepLog buildEntity(LocalDate date, LocalTime bedTime, LocalTime wakeTime) {
        int minutes = (int) java.time.Duration.between(bedTime, wakeTime).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        return new SleepLog(null, userId, date, bedTime, wakeTime, minutes, SleepStatus.OK, null, null);
    }
}
