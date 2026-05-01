package com.example.springtest.repository;

import com.example.springtest.model.Reward;
import com.example.springtest.model.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByUserId(Long userId);
    List<Reward> findByType(RewardType type);
    List<Reward> findByUserIdAndType(Long userId, RewardType type);
}
