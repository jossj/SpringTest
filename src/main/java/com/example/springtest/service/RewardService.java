package com.example.springtest.service;

import com.example.springtest.model.Reward;

import java.util.List;
import java.util.Optional;

public interface RewardService {
    List<Reward> getAllRewards();
    Optional<Reward> getRewardById(Long id);
    List<Reward> getRewardsByStudentId(Long studentId);
    Reward createReward(Reward reward);
    void deleteReward(Long id);
}
