package com.example.springtest.repository;

import com.example.springtest.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Optional<ClassRoom> findByName(String name);
}
