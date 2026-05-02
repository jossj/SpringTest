package com.example.springtest.controller;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
import com.example.springtest.model.User;
import com.example.springtest.service.RewardService;
import com.example.springtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final RewardService rewardService;

    @GetMapping
    public String dashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Reward> rewards = rewardService.getAllRewards();
        int totalPoints = rewards.stream().mapToInt(r -> r.getPoints() != null ? r.getPoints() : 0).sum();

        Map<String, Integer> leaderboard = rewards.stream()
                .filter(r -> r.getUser() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getUser().getUsername(),
                        Collectors.summingInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

        // per-user, per-type points matrix for the Rewards tile (string keys for Thymeleaf map lookup)
        List<String> rewardTypes = Arrays.stream(RewardType.values()).map(Enum::name).collect(Collectors.toList());
        Map<String, Map<String, Integer>> rewardMatrix = rewards.stream()
                .filter(r -> r.getUser() != null && r.getType() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getUser().getUsername(),
                        Collectors.groupingBy(
                                r -> r.getType().name(),
                                Collectors.summingInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                        )
                ));

        List<String> rewardUsers = new ArrayList<>(leaderboard.keySet());

        model.addAttribute("users", users);
        model.addAttribute("rewards", rewards);
        model.addAttribute("userCount", users.size());
        model.addAttribute("rewardCount", rewards.size());
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("rewardTypes", rewardTypes);
        model.addAttribute("rewardMatrix", rewardMatrix);
        model.addAttribute("rewardUsers", rewardUsers);
        return "dashboard";
    }
}
