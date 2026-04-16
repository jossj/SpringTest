package com.example.springtest.service;

import com.example.springtest.exception.ResourceNotFoundException;
import com.example.springtest.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> database = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserServiceImpl() {
        // Seed some initial data
        createUser(User.builder().username("alice").email("alice@example.com").role("USER").build());
        createUser(User.builder().username("bob").email("bob@example.com").role("USER").build());
        createUser(User.builder().username("admin").email("admin@example.com").role("ADMIN").build());
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public User createUser(User user) {
        Long id = idGenerator.getAndIncrement();
        User newUser = User.builder()
                .id(id)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole() : "USER")
                .build();
        database.put(id, newUser);
        log.info("Created user: {}", newUser);
        return newUser;
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Updating user with id: {}", id);
        User existingUser = database.get(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        
        User updatedUser = User.builder()
                .id(id)
                .username(user.getUsername() != null ? user.getUsername() : existingUser.getUsername())
                .email(user.getEmail() != null ? user.getEmail() : existingUser.getEmail())
                .role(user.getRole() != null ? user.getRole() : existingUser.getRole())
                .build();
        
        database.put(id, updatedUser);
        log.info("Updated user: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User removed = database.remove(id);
        if (removed == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }
}
