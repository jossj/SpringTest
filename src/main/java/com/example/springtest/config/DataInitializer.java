package com.example.springtest.config;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
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

            rewardRepository.save(Reward.builder().title("First Login").description("Logged in for the first time").points(10).type(RewardType.BEHAVIOR).user(alice).build());
            rewardRepository.save(Reward.builder().title("Profile Complete").description("Completed profile setup").points(25).type(RewardType.ACADEMIC).user(alice).build());
            rewardRepository.save(Reward.builder().title("Homework Streak").description("Completed homework 7 days in a row").points(30).type(RewardType.HOMEWORK).user(alice).build());
            rewardRepository.save(Reward.builder().title("Team Player").description("Participated in team sports").points(20).type(RewardType.SPORTS).user(alice).build());
            rewardRepository.save(Reward.builder().title("First Login").description("Logged in for the first time").points(10).type(RewardType.BEHAVIOR).user(bob).build());
            rewardRepository.save(Reward.builder().title("Power User").description("Used the app 30 days in a row").points(100).type(RewardType.SPORTS).user(bob).build());
            rewardRepository.save(Reward.builder().title("Top Marks").description("Achieved top score on assessment").points(50).type(RewardType.ACADEMIC).user(bob).build());
            rewardRepository.save(Reward.builder().title("Admin Badge").description("Granted admin privileges").points(50).type(RewardType.BEHAVIOR).user(admin).build());
            rewardRepository.save(Reward.builder().title("Study Group").description("Led a study group session").points(40).type(RewardType.ACADEMIC).user(admin).build());
        };
    }
}
