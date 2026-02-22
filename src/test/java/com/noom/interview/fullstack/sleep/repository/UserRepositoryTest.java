package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

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
        User saved = userRepository.save(buildEntity("test", "test@example.com"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("test");
        assertThat(saved.getFirstName()).isEqualTo("test");
        assertThat(saved.getLastName()).isEqualTo("test");
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findAll() {
        userRepository.save(buildEntity("alice", "alice@example.com"));
        userRepository.save(buildEntity("bob", "bob@example.com"));

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername)
                .containsExactlyInAnyOrder("alice", "bob");
    }

    private User buildEntity(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail(email);
        return user;
    }
}
