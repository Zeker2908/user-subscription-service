package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends ApiException {
    public SubscriptionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
  public SubscriptionNotFoundException() {
    super("Subscription not found", HttpStatus.NOT_FOUND);
  }
}
