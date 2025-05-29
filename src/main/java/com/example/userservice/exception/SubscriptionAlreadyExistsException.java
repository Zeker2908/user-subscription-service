package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExistsException extends ApiException {
    public SubscriptionAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
    public SubscriptionAlreadyExistsException() {
      super("Subscription already exists", HttpStatus.CONFLICT);
    }
}
