package com.example.springtest.service;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;

import java.util.List;
import java.util.Optional;

public interface RewardService {
    List<Reward> getAllRewards();
    Optional<Reward> getRewardById(Long id);
    List<Reward> getRewardsByUser(Long userId);
    List<Reward> getRewardsByType(RewardType type);
    List<Reward> getRewardsByUserAndType(Long userId, RewardType type);
    Reward createReward(Long userId, Reward reward);
    Reward updateReward(Long id, Reward reward);
    void deleteReward(Long id);
}
