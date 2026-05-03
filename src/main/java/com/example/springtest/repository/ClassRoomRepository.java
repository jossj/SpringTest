package com.example.springtest.repository;

import com.example.springtest.model.ClassRoom;
import com.example.springtest.model.YearLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Optional<ClassRoom> findByName(String name);
    Optional<ClassRoom> findByNameAndYearLevel(String name, YearLevel yearLevel);
}
