package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.UserResponseDto;
import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException;
import com.noom.interview.fullstack.sleep.util.TestUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM sleep_logs");
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    void create() {
        UserResponseDto response = userService.create(TestUserFactory.buildCreateRequest("rares", "rares@example.com"));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo("rares");
        assertThat(response.getFirstName()).isEqualTo("test");
        assertThat(response.getLastName()).isEqualTo("test");
        assertThat(response.getEmail()).isEqualTo("rares@example.com");
    }

    @Test
    void findAll() {
        userService.create(TestUserFactory.buildCreateRequest("rares", "rares@example.com"));
        userService.create(TestUserFactory.buildCreateRequest("rares2", "rares2@example.com"));

        List<UserResponseDto> users = userService.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(UserResponseDto::getUsername)
                .containsExactlyInAnyOrder("rares", "rares2");
    }

    @Test
    void createShouldThrowUserAlreadyExistsException() {
        userService.create(TestUserFactory.buildCreateRequest("rares", "rares@example.com"));

        assertThatThrownBy(() -> userService.create(TestUserFactory.buildCreateRequest("rares", "other@example.com")))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}
