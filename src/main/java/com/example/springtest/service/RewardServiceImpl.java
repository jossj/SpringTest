package com.example.springtest.service;

import com.example.springtest.model.Reward;
import com.example.springtest.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;

    @Override
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    @Override
    public Optional<Reward> getRewardById(Long id) {
        return rewardRepository.findById(id);
    }

    @Override
    public List<Reward> getRewardsByStudentId(Long studentId) {
        return rewardRepository.findByStudentId(studentId);
    }

    @Override
    public Reward createReward(Reward reward) {
        Reward saved = rewardRepository.save(reward);
        log.info("Created reward: {}", saved);
        return saved;
    }

    @Override
    public void deleteReward(Long id) {
        rewardRepository.deleteById(id);
    }
}
