package com.example.userservice.repository;

import com.example.userservice.dto.PopularSubscriptionDto;
import com.example.userservice.model.ServiceType;
import com.example.userservice.model.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    @Query("""
        SELECT new com.example.userservice.dto.PopularSubscriptionDto(s.serviceType, COUNT(s))\s
        FROM Subscription s\s
        GROUP BY s.serviceType\s
        ORDER BY COUNT(s) DESC
   \s""")
    List<PopularSubscriptionDto> findTopPopularSubscriptions(Pageable pageable);

    List<Subscription> findAllByUserId(UUID userId);

    Boolean existsByUserIdAndServiceType(UUID userId, ServiceType serviceType);

    Boolean existsByIdAndUserId(UUID id, UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);

    void deleteAllByUserId(UUID userId);
}
