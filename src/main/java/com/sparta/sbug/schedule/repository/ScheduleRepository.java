package com.sparta.sbug.schedule.repository;

import com.sparta.sbug.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
    @Query(nativeQuery = true, value = "select * from schedule s where id =:scheduleId")
    Optional<Schedule> findById(@Param("scheduleId") Long scheduleId);

    /**
     * 대상 유저의 일정들을 조회
     *
     * @param userId   대상 유저 ID
     * @param pageable 페이징 정보
     * @return Page&lt;Schedule&gt;
     */
    @Query(nativeQuery = true, value = "select * from schedule where user_id=:userId")
    Page<Schedule> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 특정 기간 내의 일정을 조회
     *
     * @param userId 요청자 ID
     * @param startDate 시작 시간
     * @param endDate   끝 시간
     * @param pageable  페이징 정보
     * @return Page&lt;Schedule&gt;
     */
    @Query(nativeQuery = true, value = "select * from schedule where user_id=:userId and date between :startDate and :endDate")
    Page<Schedule> findAllByUserIdAndDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
    @Query(nativeQuery = true, value = "select * from schedule where user_id=:userId and date between :startDate and :endDate")
    List<Schedule> findAllByUserIdAndDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

}
