package com.example.springtest.controller;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
import com.example.springtest.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    // GET /api/rewards
    @GetMapping
    public List<Reward> getAllRewards() {
        return rewardService.getAllRewards();
    }

    // GET /api/rewards/{id}
    @GetMapping("/{id}")
    public Reward getRewardById(@PathVariable Long id) {
        return rewardService.getRewardById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));
    }

    // GET /api/rewards/user/{userId}
    @GetMapping("/user/{userId}")
    public List<Reward> getRewardsByUser(@PathVariable Long userId) {
        return rewardService.getRewardsByUser(userId);
    }

    // GET /api/rewards/type/{type}
    @GetMapping("/type/{type}")
    public List<Reward> getRewardsByType(@PathVariable RewardType type) {
        return rewardService.getRewardsByType(type);
    }

    // GET /api/rewards/user/{userId}/type/{type}
    @GetMapping("/user/{userId}/type/{type}")
    public List<Reward> getRewardsByUserAndType(@PathVariable Long userId, @PathVariable RewardType type) {
        return rewardService.getRewardsByUserAndType(userId, type);
    }

    // POST /api/rewards/user/{userId}
    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reward createReward(@PathVariable Long userId, @RequestBody Reward reward) {
        return rewardService.createReward(userId, reward);
    }

    // PUT /api/rewards/{id}
    @PutMapping("/{id}")
    public Reward updateReward(@PathVariable Long id, @RequestBody Reward reward) {
        return rewardService.updateReward(id, reward);
    }

    // DELETE /api/rewards/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
    }
}
