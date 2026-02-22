package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateUserRequestDto;
import com.noom.interview.fullstack.sleep.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> findAll();

    UserResponseDto create(CreateUserRequestDto dto);

}
