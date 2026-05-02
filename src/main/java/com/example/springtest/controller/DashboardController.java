package com.example.springtest.controller;

import com.example.springtest.model.Reward;
import com.example.springtest.model.User;
import com.example.springtest.service.RewardService;
import com.example.springtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

        model.addAttribute("users", users);
        model.addAttribute("rewards", rewards);
        model.addAttribute("userCount", users.size());
        model.addAttribute("rewardCount", rewards.size());
        model.addAttribute("totalPoints", totalPoints);
        return "dashboard";
    }
}
