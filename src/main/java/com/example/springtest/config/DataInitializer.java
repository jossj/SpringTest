package com.example.springtest.config;

import com.example.springtest.model.*;
import com.example.springtest.repository.ClassRoomRepository;
import com.example.springtest.repository.RewardRepository;
import com.example.springtest.repository.StudentRepository;
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
    private final ClassRoomRepository classRoomRepository;
    private final StudentRepository studentRepository;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder().username("alice").email("alice@example.com").role("USER").build());
                userRepository.save(User.builder().username("bob").email("bob@example.com").role("USER").build());
                userRepository.save(User.builder().username("admin").email("admin@example.com").role("ADMIN").build());
            }

            // Re-seed rewards whenever any row is missing a type (handles schema migrations)
            boolean needsReseed = rewardRepository.findAll().stream().anyMatch(r -> r.getType() == null);
            if (rewardRepository.count() == 0 || needsReseed) {
                rewardRepository.deleteAll();

                User alice = userRepository.findByUsername("alice").orElseThrow();
                User bob   = userRepository.findByUsername("bob").orElseThrow();
                User admin = userRepository.findByUsername("admin").orElseThrow();

                rewardRepository.save(Reward.builder().title("First Login").description("Logged in for the first time").points(10).type(RewardType.BEHAVIOR).user(alice).build());
                rewardRepository.save(Reward.builder().title("Profile Complete").description("Completed profile setup").points(25).type(RewardType.ACADEMIC).user(alice).build());
                rewardRepository.save(Reward.builder().title("Homework Streak").description("Completed homework 7 days in a row").points(30).type(RewardType.HOMEWORK).user(alice).build());
                rewardRepository.save(Reward.builder().title("Team Player").description("Participated in team sports").points(20).type(RewardType.SPORTS).user(alice).build());
                rewardRepository.save(Reward.builder().title("First Login").description("Logged in for the first time").points(10).type(RewardType.BEHAVIOR).user(bob).build());
                rewardRepository.save(Reward.builder().title("Power User").description("Used the app 30 days in a row").points(100).type(RewardType.SPORTS).user(bob).build());
                rewardRepository.save(Reward.builder().title("Top Marks").description("Achieved top score on assessment").points(50).type(RewardType.ACADEMIC).user(bob).build());
                rewardRepository.save(Reward.builder().title("Admin Badge").description("Granted admin privileges").points(50).type(RewardType.BEHAVIOR).user(admin).build());
                rewardRepository.save(Reward.builder().title("Study Group").description("Led a study group session").points(40).type(RewardType.ACADEMIC).user(admin).build());
            }

            if (classRoomRepository.count() == 0) {
                ClassRoom room5A = classRoomRepository.save(ClassRoom.builder().name("5A").yearLevel(YearLevel.YEAR_5).build());
                ClassRoom room5B = classRoomRepository.save(ClassRoom.builder().name("5B").yearLevel(YearLevel.YEAR_5).build());
                ClassRoom room6A = classRoomRepository.save(ClassRoom.builder().name("6A").yearLevel(YearLevel.YEAR_6).build());
                ClassRoom room7A = classRoomRepository.save(ClassRoom.builder().name("7A").yearLevel(YearLevel.YEAR_7).build());
                ClassRoom room8B = classRoomRepository.save(ClassRoom.builder().name("8B").yearLevel(YearLevel.YEAR_8).build());

                studentRepository.save(Student.builder().firstName("Emma").lastName("Johnson").email("emma.johnson@school.com").classRoom(room5A).build());
                studentRepository.save(Student.builder().firstName("Liam").lastName("Smith").email("liam.smith@school.com").classRoom(room5A).build());
                studentRepository.save(Student.builder().firstName("Olivia").lastName("Brown").email("olivia.brown@school.com").classRoom(room5B).build());
                studentRepository.save(Student.builder().firstName("Noah").lastName("Williams").email("noah.williams@school.com").classRoom(room5B).build());
                studentRepository.save(Student.builder().firstName("Ava").lastName("Jones").email("ava.jones@school.com").classRoom(room6A).build());
                studentRepository.save(Student.builder().firstName("William").lastName("Davis").email("william.davis@school.com").classRoom(room6A).build());
                studentRepository.save(Student.builder().firstName("Sophia").lastName("Miller").email("sophia.miller@school.com").classRoom(room7A).build());
                studentRepository.save(Student.builder().firstName("James").lastName("Wilson").email("james.wilson@school.com").classRoom(room7A).build());
                studentRepository.save(Student.builder().firstName("Isabella").lastName("Moore").email("isabella.moore@school.com").classRoom(room8B).build());
                studentRepository.save(Student.builder().firstName("Oliver").lastName("Taylor").email("oliver.taylor@school.com").classRoom(room8B).build());
            }
        };
    }
}
