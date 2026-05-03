package com.example.springtest.controller;

import com.example.springtest.model.Reward;
import com.example.springtest.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping
    public List<Reward> getAllRewards() {
        return rewardService.getAllRewards();
    }

    @GetMapping("/{id}")
    public Reward getRewardById(@PathVariable Long id) {
        return rewardService.getRewardById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));
    }

    @GetMapping("/student/{studentId}")
    public List<Reward> getRewardsByStudent(@PathVariable Long studentId) {
        return rewardService.getRewardsByStudentId(studentId);
    }

    @PostMapping
    public Reward createReward(@RequestBody Reward reward) {
        return rewardService.createReward(reward);
    }

    @DeleteMapping("/{id}")
    public void deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
    }
}
