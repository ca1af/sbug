package com.sparta.sbug.schedule.repository;

import com.sparta.sbug.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * 일정 ID로 일정을 조회
     *
     * @param scheduleId 일정 ID
     * @return Optional&lt;Schedule&gt;
     */
    Optional<Schedule> findById(Long scheduleId);

    /**
     * 대상 유저의 일정들을 조회
     *
     * @param userId   대상 유저 ID
     * @param pageable 페이징 정보
     * @return Page&lt;Schedule&gt;
     */
    Page<Schedule> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 특정 기간 내의 일정을 조회
     *
     * @param userId 요청자 ID
     * @param startDate 시작 시간
     * @param endDate   끝 시간
     * @param pageable  페이징 정보
     * @return Page&lt;Schedule&gt;
     */
    Page<Schedule> findAllByUserIdAndDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    List<Schedule> findAllByUserIdAndDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

}
