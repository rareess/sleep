package com.noom.interview.fullstack.sleep.util;

import com.noom.interview.fullstack.sleep.dto.CreateUserRequestDto;
import com.noom.interview.fullstack.sleep.model.User;

public final class TestUserFactory {

    private TestUserFactory() {}

    public static User buildUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail(email);
        return user;
    }

    public static CreateUserRequestDto buildCreateRequest(String username, String email) {
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setUsername(username);
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail(email);
        return dto;
    }
}
