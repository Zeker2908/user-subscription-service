package com.example.userservice.dto;

import com.example.userservice.model.ServiceType;
import com.example.userservice.model.SubscriptionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriptionResponse {
    private UUID id;
    private ServiceType serviceType;
    private BigDecimal price;
    private Boolean autoRenew;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private UUID userId;
}
