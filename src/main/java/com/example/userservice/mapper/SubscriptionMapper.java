package com.example.userservice.mapper;

import com.example.userservice.dto.SubscriptionRequest;
import com.example.userservice.dto.SubscriptionResponse;
import com.example.userservice.model.Subscription;
import com.example.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Subscription toEntity(SubscriptionRequest dto, User user);

    @Mapping(source = "user.id", target = "userId")
    SubscriptionResponse toDto(Subscription entity);
}
