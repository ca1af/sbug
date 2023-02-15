package com.sparta.sbug.schedule.repository;

import com.sparta.sbug.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findById(Long scheduleId);
    Page<Schedule> findAllByUserId(Long userId, Pageable pageable);
    Page<Schedule> findAllByUserIdAndDateBetween(
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );
}
