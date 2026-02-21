package com.noom.interview.fullstack.sleep.controller.user;

import com.noom.interview.fullstack.sleep.dto.UserResponseDto;
import com.noom.interview.fullstack.sleep.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
