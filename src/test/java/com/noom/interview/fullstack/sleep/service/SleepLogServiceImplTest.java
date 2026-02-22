package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.SleepLogAveragesResponseDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;
import com.noom.interview.fullstack.sleep.exception.DuplicateSleepLogException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SleepLogServiceImplTest {

    @Autowired
    private SleepLogService sleepLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID userId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM sleep_logs");
        jdbcTemplate.execute("DELETE FROM users");

        userId = userService.create(TestUserFactory.buildCreateRequest("rares", "rares@example.com")).getId();
    }

    @Test
    void save() {
        SleepLogResponseDto response = sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getSleepDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(response.getMinutesInBed()).isEqualTo(480);
        assertThat(response.getSleepStatus()).isEqualTo(SleepStatus.OK);
    }

    @Test
    void saveWhenUserDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        assertThatThrownBy(() -> sleepLogService.create(nonExistentId, TestSleepLogFactory.buildCreateRequest(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0))))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveWhenLogAlreadyExistsForDate() {
        LocalDate date = LocalDate.now().minusDays(1);
        sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(date, LocalTime.of(22, 0), LocalTime.of(6, 0)));

        assertThatThrownBy(() -> sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(date, LocalTime.of(21, 0), LocalTime.of(5, 0))))
                .isInstanceOf(DuplicateSleepLogException.class);
    }

    @Test
    void getLastNights() {
        sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));
        sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(LocalDate.now().minusDays(2), LocalTime.of(23, 0), LocalTime.of(7, 0)));

        List<SleepLogResponseDto> result = sleepLogService.getLastNights(userId, LocalDate.now().minusDays(7), LocalDate.now().minusDays(1));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SleepLogResponseDto::getSleepDate)
                .containsExactly(LocalDate.now().minusDays(1), LocalDate.now().minusDays(2));
    }

    @Test
    void getAveragesShouldReturnZeroesWhenNoLogsInRange() {
        SleepLogAveragesResponseDto result = sleepLogService.getAverages(userId, LocalDate.now().minusDays(7), LocalDate.now().minusDays(1));

        assertThat(result.getAverageMinutesInBed()).isEqualTo(0);
        assertThat(result.getAverageBedTime()).isNull();
        assertThat(result.getAverageWakeTime()).isNull();
        assertThat(result.getBadCount()).isEqualTo(0);
        assertThat(result.getOkCount()).isEqualTo(0);
        assertThat(result.getGoodCount()).isEqualTo(0);
    }

    @Test
    void getAveragesShouldReturnComputedAverages() {
        sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0), SleepStatus.OK));
        sleepLogService.create(userId, TestSleepLogFactory.buildCreateRequest(LocalDate.now().minusDays(2), LocalTime.of(23, 0), LocalTime.of(7, 0), SleepStatus.GOOD));

        SleepLogAveragesResponseDto result = sleepLogService.getAverages(userId, LocalDate.now().minusDays(7), LocalDate.now().minusDays(1));

        assertThat(result.getAverageMinutesInBed()).isEqualTo(480);
        assertThat(result.getAverageBedTime()).isEqualTo(LocalTime.of(22, 30));
        assertThat(result.getAverageWakeTime()).isEqualTo(LocalTime.of(6, 30));
        assertThat(result.getOkCount()).isEqualTo(1);
        assertThat(result.getGoodCount()).isEqualTo(1);
        assertThat(result.getBadCount()).isEqualTo(0);
    }

    @Test
    void getAveragesShouldThrowUserNotFoundWhenUserDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        assertThatThrownBy(() -> sleepLogService.getAverages(nonExistentId, LocalDate.now().minusDays(7), LocalDate.now().minusDays(1)))
                .isInstanceOf(UserNotFoundException.class);
    }
}
