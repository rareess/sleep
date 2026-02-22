package com.noom.interview.fullstack.sleep.repository.impl;

import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.repository.mapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, username, first_name, last_name, email FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = "SELECT id, username, first_name, last_name, email FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, params, userRowMapper).stream().findFirst();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, first_name, last_name, email) " +
                     "VALUES (:username, :firstName, :lastName, :email) RETURNING id, username, first_name, last_name, email";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail());

        return jdbcTemplate.queryForObject(sql, params, userRowMapper);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.findById(id).isPresent();
    }

}
