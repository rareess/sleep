package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<User> findAll();
    Optional<User> findById(UUID id);
    User save(User user);
    boolean existsById(UUID id);
}
