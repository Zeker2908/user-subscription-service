package com.example.userservice.controller;

import com.example.userservice.dto.PopularSubscriptionDto;
import com.example.userservice.dto.SubscriptionRequest;
import com.example.userservice.dto.SubscriptionResponse;
import com.example.userservice.mapper.SubscriptionMapper;
import com.example.userservice.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @PostMapping("/users/{id}/subscriptions")
    public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable UUID id, @RequestBody @Valid SubscriptionRequest subscriptionRequest) {
        log.info("Received request to create a subscription for user with ID {}", id);
        return ResponseEntity.ok(subscriptionMapper.toDto(subscriptionService.create(id,subscriptionRequest)));
    }

    @GetMapping("/users/{id}/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptions(@PathVariable UUID id) {
        log.info("Received request to get all subscriptions for user with ID {}", id);
        return ResponseEntity.ok(subscriptionService.findAllByUserId(id).stream().map(subscriptionMapper::toDto).toList());
    }

    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<PopularSubscriptionDto>> getTopSubscriptions(@RequestParam(defaultValue = "3") int size) {
        log.info("Received request to get top subscriptions");
        return ResponseEntity.ok(subscriptionService.findTopPopularSubscriptions(PageRequest.of(0, size)));
    }

    @DeleteMapping("/users/{userId}/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID userId, @PathVariable UUID subscriptionId) {
        log.info("Received request to delete a subscription for user with ID {} and ID {}", userId, subscriptionId);
        subscriptionService.delete(subscriptionId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}/subscriptions")
    public ResponseEntity<Void> deleteAllSubscriptions(@PathVariable UUID userId) {
        log.info("Received request to delete all subscriptions for user with ID {}", userId);
        subscriptionService.deleteAllByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
