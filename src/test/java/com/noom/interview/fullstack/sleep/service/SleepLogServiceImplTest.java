package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.CreateUserRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;
import com.noom.interview.fullstack.sleep.exception.DuplicateSleepLogException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.model.SleepStatus;
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

        CreateUserRequestDto userDto = new CreateUserRequestDto();
        userDto.setUsername("testuser");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setEmail("test@example.com");
        userId = userService.create(userDto).getId();
    }

    @Test
    void save() {
        SleepLogResponseDto response = sleepLogService.create(userId, buildEntity(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getSleepDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(response.getMinutesInBed()).isEqualTo(480);
        assertThat(response.getSleepStatus()).isEqualTo(SleepStatus.OK);
    }

    @Test
    void saveWhenUserDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        assertThatThrownBy(() -> sleepLogService.create(nonExistentId, buildEntity(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0))))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveWhenLogAlreadyExistsForDate() {
        LocalDate date = LocalDate.now().minusDays(1);
        sleepLogService.create(userId, buildEntity(date, LocalTime.of(22, 0), LocalTime.of(6, 0)));

        assertThatThrownBy(() -> sleepLogService.create(userId, buildEntity(date, LocalTime.of(21, 0), LocalTime.of(5, 0))))
                .isInstanceOf(DuplicateSleepLogException.class);
    }

    @Test
    void getLastNights() {
        sleepLogService.create(userId, buildEntity(LocalDate.now().minusDays(1), LocalTime.of(22, 0), LocalTime.of(6, 0)));
        sleepLogService.create(userId, buildEntity(LocalDate.now().minusDays(2), LocalTime.of(23, 0), LocalTime.of(7, 0)));

        List<SleepLogResponseDto> result = sleepLogService.getLastNights(userId, 7);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SleepLogResponseDto::getSleepDate)
                .containsExactly(LocalDate.now().minusDays(1), LocalDate.now().minusDays(2));
    }

    private CreateSleepLogRequestDto buildEntity(LocalDate date, LocalTime bedTime, LocalTime wakeTime) {
        CreateSleepLogRequestDto dto = new CreateSleepLogRequestDto();
        dto.setSleepDate(date);
        dto.setBedTime(bedTime);
        dto.setWakeTime(wakeTime);
        dto.setSleepStatus(SleepStatus.OK);
        return dto;
    }
}
