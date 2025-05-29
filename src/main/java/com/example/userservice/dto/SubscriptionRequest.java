package com.example.userservice.dto;

import com.example.userservice.model.ServiceType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SubscriptionRequest {

    @NotNull(message = "Service type must not be null")
    private ServiceType serviceType;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;

    @NotNull(message = "Auto-renew flag must not be null")
    private Boolean autoRenew;

    @NotNull(message = "Start date must not be null")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    private LocalDate endDate;

    @AssertTrue(message = "End date must be after start date")
    public boolean isDatesValid() {
        return endDate.isAfter(startDate);
    }
}

