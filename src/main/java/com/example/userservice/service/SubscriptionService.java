package com.example.userservice.service;

import com.example.userservice.dto.PopularSubscriptionDto;
import com.example.userservice.dto.SubscriptionRequest;
import com.example.userservice.exception.SubscriptionAlreadyExistsException;
import com.example.userservice.exception.SubscriptionNotFoundException;
import com.example.userservice.mapper.SubscriptionMapper;
import com.example.userservice.model.Subscription;
import com.example.userservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;

    @Transactional
    public Subscription create(UUID userID,SubscriptionRequest request) {
        log.debug("Checking if subscription exists with userId {} and service type {}", userID, request.getServiceType());
        if(subscriptionRepository.existsByUserIdAndServiceType(userID, request.getServiceType())) {
            log.warn("Subscription {} already exists with service type {}", userID, request.getServiceType());
            throw new SubscriptionAlreadyExistsException();
        }
        log.info("Creating new subscription with userId {} and service type {}", userID, request.getServiceType());
        Subscription subscription = subscriptionRepository.save(subscriptionMapper.toEntity(request,userService.findById(userID)));
        log.info("Subscription created with id {} for user with ID {}", subscription.getId(), userID);
        return subscription;
    }

    public List<Subscription> findAllByUserId(UUID userId) {
        log.info("Finding all subscriptions with userId {}", userId);
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(userId);
        log.info("Found {} subscriptions", subscriptions.size());
        return subscriptions;
    }

    public List<PopularSubscriptionDto> findTopPopularSubscriptions(Pageable pageable) {
        log.info("Finding top popular subscriptions");
        List<PopularSubscriptionDto> subscription = subscriptionRepository.findTopPopularSubscriptions(pageable);
        log.info("Found {} most popular subscriptions", subscription.size());
        return subscription;
    }

    @Transactional
    public void delete(UUID id, UUID userId) {
        log.debug("Checking if subscription exists with userId {} and id {}", userId, id);
        if(!subscriptionRepository.existsByIdAndUserId(id, userId)) {
            log.warn("Subscription {} does not exist with id {}", id, userId);
            throw new SubscriptionNotFoundException("Subscription with id:" + id + " and userId:" + userId + " not found");
        }
        log.info("Deleting subscription with id {}", id);
        subscriptionRepository.deleteByIdAndUserId(id, userId);
        log.info("Subscription {} deleted", id);
    }

    @Transactional
    public void deleteAllByUserId(UUID userId) {
        log.info("Deleting all subscriptions with userId {}", userId);
        subscriptionRepository.deleteAllByUserId(userId);
        log.info("All subscriptions deleted for user with ID {}", userId);
    }
}
