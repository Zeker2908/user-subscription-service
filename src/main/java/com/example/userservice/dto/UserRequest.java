package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "The email address must be in the format user@example.com")
    @Size(min = 5, max = 255, message = "Email address must be between 5 and 255 characters long")
    private String email;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 255, message = "The name must be between 2 and 255 characters long.")
    private String name;

}
