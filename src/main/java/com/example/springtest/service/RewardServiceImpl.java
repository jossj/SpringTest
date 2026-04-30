package com.example.springtest.service;

import com.example.springtest.exception.ResourceNotFoundException;
import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
import com.example.springtest.model.User;
import com.example.springtest.repository.RewardRepository;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;

    @Override
    public List<Reward> getAllRewards() {
        log.info("Fetching all rewards");
        return rewardRepository.findAll();
    }

    @Override
    public Optional<Reward> getRewardById(Long id) {
        log.info("Fetching reward with id: {}", id);
        return rewardRepository.findById(id);
    }

    @Override
    public List<Reward> getRewardsByUser(Long userId) {
        log.info("Fetching rewards for user id: {}", userId);
        return rewardRepository.findByUserId(userId);
    }

    @Override
    public List<Reward> getRewardsByType(RewardType type) {
        log.info("Fetching rewards of type: {}", type);
        return rewardRepository.findByType(type);
    }

    @Override
    public List<Reward> getRewardsByUserAndType(Long userId, RewardType type) {
        log.info("Fetching rewards for user id: {} of type: {}", userId, type);
        return rewardRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public Reward createReward(Long userId, Reward reward) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        reward.setUser(user);
        if (reward.getAwardedAt() == null) {
            reward.setAwardedAt(LocalDateTime.now());
        }

        Reward saved = rewardRepository.save(reward);
        log.info("Created reward: {}", saved);
        return saved;
    }

    @Override
    public Reward updateReward(Long id, Reward reward) {
        log.info("Updating reward with id: {}", id);
        Reward existing = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + id));

        if (reward.getType() != null) existing.setType(reward.getType());
        if (reward.getDescription() != null) existing.setDescription(reward.getDescription());
        if (reward.getPoints() > 0) existing.setPoints(reward.getPoints());

        Reward updated = rewardRepository.save(existing);
        log.info("Updated reward: {}", updated);
        return updated;
    }

    @Override
    public void deleteReward(Long id) {
        log.info("Deleting reward with id: {}", id);
        if (!rewardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reward not found with id: " + id);
        }
        rewardRepository.deleteById(id);
    }
}
