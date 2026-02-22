package com.noom.interview.fullstack.sleep.controller.user;

import com.noom.interview.fullstack.sleep.dto.CreateUserRequestDto;
import com.noom.interview.fullstack.sleep.dto.UserResponseDto;
import com.noom.interview.fullstack.sleep.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody CreateUserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

}
