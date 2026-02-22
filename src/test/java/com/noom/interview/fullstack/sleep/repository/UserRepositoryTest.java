package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.util.TestUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM sleep_logs");
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    void save() {
        User saved = userRepository.save(TestUserFactory.buildUser("rares", "rares@example.com"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("rares");
        assertThat(saved.getFirstName()).isEqualTo("test");
        assertThat(saved.getLastName()).isEqualTo("test");
        assertThat(saved.getEmail()).isEqualTo("rares@example.com");
    }

    @Test
    void findAll() {
        userRepository.save(TestUserFactory.buildUser("rares", "rares@example.com"));
        userRepository.save(TestUserFactory.buildUser("rares2", "rares2@example.com"));

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername)
                .containsExactlyInAnyOrder("rares", "rares2");
    }

    @Test
    void findByIdShouldReturnUser() {
        User saved = userRepository.save(TestUserFactory.buildUser("rares", "rares@example.com"));

        Optional<User> result = userRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("rares");
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotFound() {
        Optional<User> result = userRepository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void existsByIdShouldReturnTrueWhenUserExists() {
        User saved = userRepository.save(TestUserFactory.buildUser("rares", "rares@example.com"));

        assertThat(userRepository.existsById(saved.getId())).isTrue();
    }

    @Test
    void existsByIdShouldReturnFalseWhenUserDoesNotExist() {
        assertThat(userRepository.existsById(UUID.fromString("00000000-0000-0000-0000-000000000009"))).isFalse();
    }
}
