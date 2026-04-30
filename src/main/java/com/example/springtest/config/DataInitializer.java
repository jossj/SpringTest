package com.example.springtest.config;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
import com.example.springtest.model.User;
import com.example.springtest.repository.RewardRepository;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User alice = userRepository.save(User.builder().username("alice").email("alice@example.com").role("USER").build());
            User bob   = userRepository.save(User.builder().username("bob").email("bob@example.com").role("USER").build());
            User admin = userRepository.save(User.builder().username("admin").email("admin@example.com").role("ADMIN").build());

            rewardRepository.saveAll(List.of(
                // Alice's rewards
                reward(alice, RewardType.ACADEMIC,  "Top marks in maths test",         50, daysAgo(10)),
                reward(alice, RewardType.HOMEWORK,  "Completed all homework on time",   20, daysAgo(7)),
                reward(alice, RewardType.BEHAVIOR,  "Helped a classmate",               15, daysAgo(5)),
                reward(alice, RewardType.SPORTS,    "First place in 100m sprint",       40, daysAgo(2)),

                // Bob's rewards
                reward(bob,   RewardType.BEHAVIOR,  "Outstanding classroom conduct",    25, daysAgo(12)),
                reward(bob,   RewardType.SPORTS,    "Man of the match — football",      35, daysAgo(8)),
                reward(bob,   RewardType.HOMEWORK,  "Perfect homework streak (2 weeks)", 30, daysAgo(3)),
                reward(bob,   RewardType.ACADEMIC,  "Science project excellence",       45, daysAgo(1)),

                // Admin's rewards
                reward(admin, RewardType.ACADEMIC,  "Completed advanced training",      60, daysAgo(15)),
                reward(admin, RewardType.BEHAVIOR,  "Mentored new team members",        50, daysAgo(9)),
                reward(admin, RewardType.SPORTS,    "Won staff charity fun run",        20, daysAgo(4)),
                reward(admin, RewardType.HOMEWORK,  "Submitted all reports early",      25, daysAgo(1))
            ));
        }
    }

    private Reward reward(User user, RewardType type, String description, int points, LocalDateTime awardedAt) {
        return Reward.builder()
                .user(user)
                .type(type)
                .description(description)
                .points(points)
                .awardedAt(awardedAt)
                .build();
    }

    private LocalDateTime daysAgo(int days) {
        return LocalDateTime.now().minusDays(days);
    }
}
