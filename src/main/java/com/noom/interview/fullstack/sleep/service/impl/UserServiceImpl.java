package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.UserResponseDto;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }
}
