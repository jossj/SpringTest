package com.example.springtest.config;

import com.example.springtest.model.User;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder().username("alice").email("alice@example.com").role("USER").build());
            userRepository.save(User.builder().username("bob").email("bob@example.com").role("USER").build());
            userRepository.save(User.builder().username("admin").email("admin@example.com").role("ADMIN").build());
        }
    }
}
