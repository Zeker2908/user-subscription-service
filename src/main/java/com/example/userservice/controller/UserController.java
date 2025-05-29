package com.example.userservice.controller;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest user) {
        log.info("Received request to create user with email: {}", user.getEmail());
        return ResponseEntity.ok(userMapper.toDto(userService.create(user)));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to get users: page={}, size={}", page, size);
        return ResponseEntity.ok(userService.findAll(PageRequest.of(page, size)).map(userMapper::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        log.info("Received request to get user with ID: {}", id);
        return ResponseEntity.ok(userMapper.toDto(userService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody @Valid UserRequest user) {
        log.info("Received request to update user with ID: {}", id);
        return ResponseEntity.ok(userMapper.toDto(userService.update(id, user)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
