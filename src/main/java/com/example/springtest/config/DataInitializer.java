package com.example.springtest.config;

import com.example.springtest.model.Reward;
import com.example.springtest.model.User;
import com.example.springtest.repository.RewardRepository;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            if (userRepository.count() > 0) return;

            User alice = userRepository.save(User.builder().username("alice").email("alice@example.com").role("USER").build());
            User bob   = userRepository.save(User.builder().username("bob").email("bob@example.com").role("USER").build());
            User admin = userRepository.save(User.builder().username("admin").email("admin@example.com").role("ADMIN").build());

            rewardRepository.save(Reward.builder().title("First Login").description("Logged in for the first time").points(10).user(alice).build());
            rewardRepository.save(Reward.builder().title("Profile Complete").description("Completed profile setup").points(25).user(alice).build());
            rewardRepository.save(Reward.builder().title("First Login").description("Logged in for the first time").points(10).user(bob).build());
            rewardRepository.save(Reward.builder().title("Power User").description("Used the app 30 days in a row").points(100).user(bob).build());
            rewardRepository.save(Reward.builder().title("Admin Badge").description("Granted admin privileges").points(50).user(admin).build());
        };
    }
}
