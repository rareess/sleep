package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateUserRequestDto;
import com.noom.interview.fullstack.sleep.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException;

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
        UserResponseDto response = userService.create(buildEntity("test", "test@example.com"));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo("test");
        assertThat(response.getFirstName()).isEqualTo("test");
        assertThat(response.getLastName()).isEqualTo("test");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findAll() {
        userService.create(buildEntity("alice", "alice@example.com"));
        userService.create(buildEntity("bob", "bob@example.com"));

        List<UserResponseDto> users = userService.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(UserResponseDto::getUsername)
                .containsExactlyInAnyOrder("alice", "bob");
    }

    @Test
    void createShouldThrowUserAlreadyExistsException() {
        userService.create(buildEntity("alice", "alice@example.com"));

        assertThatThrownBy(() -> userService.create(buildEntity("alice", "other@example.com")))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    private CreateUserRequestDto buildEntity(String username, String email) {
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setUsername(username);
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail(email);
        return dto;
    }
}
