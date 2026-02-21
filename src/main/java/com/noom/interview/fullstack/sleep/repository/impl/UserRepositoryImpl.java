package com.noom.interview.fullstack.sleep.repository.impl;

import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.repository.mapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
