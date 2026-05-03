package com.example.springtest.controller;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
import com.example.springtest.model.User;
import com.example.springtest.model.Student;
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

    public static class RewardRow {
        public final String username;
        public final List<Integer> typePoints;
        public final int total;
        RewardRow(String username, List<Integer> typePoints, int total) {
            this.username = username;
            this.typePoints = typePoints;
            this.total = total;
        }
    }

    private final UserService userService;
    private final RewardService rewardService;

    @GetMapping
    public String dashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Reward> rewards = rewardService.getAllRewards();
        int totalPoints = rewards.stream().mapToInt(r -> r.getPoints() != null ? r.getPoints() : 0).sum();

        Map<String, Integer> leaderboard = rewards.stream()
                .filter(r -> r.getStudent() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getStudent().getFirstName() + " " + r.getStudent().getLastName(),
                        Collectors.summingInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

        // per-user, per-type points matrix for the Rewards tile (string keys for Thymeleaf map lookup)
        List<String> rewardTypes = Arrays.stream(RewardType.values()).map(Enum::name).collect(Collectors.toList());

        Map<String, Map<String, Integer>> pointsByUserAndType = rewards.stream()
                .filter(r -> r.getStudent() != null && r.getType() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getStudent().getFirstName() + " " + r.getStudent().getLastName(),
                        Collectors.groupingBy(
                                r -> r.getType().name(),
                                Collectors.summingInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                        )
                ));

        // Build rows as typed objects so the template iterates a List — no variable map key lookups
        List<RewardRow> rewardRows = new ArrayList<>(leaderboard.keySet()).stream()
                .map(username -> {
                    Map<String, Integer> typeMap = pointsByUserAndType.getOrDefault(username, Collections.emptyMap());
                    List<Integer> typePoints = rewardTypes.stream()
                            .map(typeMap::get)
                            .collect(Collectors.toList());
                    return new RewardRow(username, typePoints, leaderboard.get(username));
                })
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("rewards", rewards);
        model.addAttribute("userCount", users.size());
        model.addAttribute("rewardCount", rewards.size());
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("rewardTypes", rewardTypes);
        model.addAttribute("rewardRows", rewardRows);
        return "dashboard";
    }
}
