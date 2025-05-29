package com.example.userservice.dto;

import com.example.userservice.model.ServiceType;

public record PopularSubscriptionDto(ServiceType serviceType, long count) {
}
