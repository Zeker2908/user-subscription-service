package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
    public UserAlreadyExistsException() {
        super("User already exists", HttpStatus.CONFLICT);
    }
}