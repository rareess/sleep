package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.CreateUserRequestDto;
import com.noom.interview.fullstack.sleep.dto.UserResponseDto;
import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException;
import com.noom.interview.fullstack.sleep.model.User;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto create(CreateUserRequestDto dto) {
        log.info("Creating user with username {}", dto.getUsername());
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        try {
            UserResponseDto created = UserResponseDto.from(userRepository.save(user));
            log.info("User created with id {}", created.getId());
            return created;
        } catch (DuplicateKeyException ex) {
            log.warn("Attempt to create duplicate user with username {}", dto.getUsername());
            throw new UserAlreadyExistsException();
        }
    }
}
