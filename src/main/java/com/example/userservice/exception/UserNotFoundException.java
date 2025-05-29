package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND);
    }
}
