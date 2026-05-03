package com.example.springtest.repository;

import com.example.springtest.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByStudentId(Long studentId);
}
