package com.example.userservice.mapper;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest userRequest);
    UserResponse toDto(User user);

    void partialUpdate(@MappingTarget User user, UserRequest request);

}
