package com.example.springtest.service;

import com.example.springtest.exception.ResourceNotFoundException;
import com.example.springtest.model.User;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        if (user.getRole() == null) user.setRole("USER");
        User saved = userRepository.save(user);
        log.info("Created user: {}", saved);
        return saved;
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Updating user with id: {}", id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (user.getUsername() != null) existing.setUsername(user.getUsername());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getRole() != null) existing.setRole(user.getRole());
        User saved = userRepository.save(existing);
        log.info("Updated user: {}", saved);
        return saved;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
