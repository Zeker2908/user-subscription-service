package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User create(UserRequest userDto) {
        log.debug("Checking if user with email '{}' already exists", userDto.getEmail());
        if(userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("User creation failed: email '{}' already exists", userDto.getEmail());
            throw new UserAlreadyExistsException();
        }
        log.info("Creating user with email '{}'", userDto.getEmail());
        User savedUser = userRepository.save(userMapper.toEntity(userDto));
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    public Page<User> findAll(Pageable pageable) {
        log.info("Finding all users");
        Page<User> users = userRepository.findAll(pageable);
        log.info("Found {} users", users.getTotalElements());
        return users;
    }

    public User findById(UUID id) {
        log.info("Finding user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(()->{
            log.warn("User not found with ID: {}", id);
            return new UserNotFoundException("User with ID " + id + " not found");
        });
        log.info("Found user with ID: {}", user.getId());
        return user;
    }

    @Transactional
    public User update(UUID id, UserRequest updatedUserDto) {
        log.info("Updating user with id: {}", id);
        log.debug("Checking if user with ID '{}' exists", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User update failed: user with ID {} not found", id);
                    return new UserNotFoundException("User with ID " + id + " not found");
                });
        if (!existingUser.getEmail().equals(updatedUserDto.getEmail())
                && userRepository.existsByEmail(updatedUserDto.getEmail())) {
            log.warn("User update failed: email '{}' already exists", updatedUserDto.getEmail());
            throw new UserAlreadyExistsException("User with email " + updatedUserDto.getEmail() + " already exists");
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }


    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        log.info("User deleted with ID: {}", id);
    }
}
