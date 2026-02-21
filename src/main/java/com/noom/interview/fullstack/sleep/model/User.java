package com.noom.interview.fullstack.sleep.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;
}
