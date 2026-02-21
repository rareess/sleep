package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();
}
