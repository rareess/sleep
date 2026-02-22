package com.noom.interview.fullstack.sleep.repository.impl;

import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.repository.mapper.UserRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String FIND_ALL_SQL =
            "SELECT id, username, first_name, last_name, email FROM users";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, username, first_name, last_name, email FROM users WHERE id = :id";

    private static final String INSERT_SQL =
            "INSERT INTO users (id, username, first_name, last_name, email) " +
            "VALUES (:id, :username, :firstName, :lastName, :email)";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT EXISTS(SELECT 1 FROM users WHERE id = :id)";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.query(FIND_ALL_SQL, userRowMapper);
        log.info("Found {} users", users.size());
        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(FIND_BY_ID_SQL, params, userRowMapper).stream().findFirst();
    }

    @Override
    public User save(User user) {
        user.setId(UUID.randomUUID());

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("username", user.getUsername())
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail());

        jdbcTemplate.update(INSERT_SQL, params);
        log.info("User saved with id {}", user.getId());
        return user;
    }

    @Override
    public boolean existsById(UUID id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Boolean exists = jdbcTemplate.queryForObject(EXISTS_BY_ID_SQL, params, Boolean.class);
        return Boolean.TRUE.equals(exists);
    }
}
