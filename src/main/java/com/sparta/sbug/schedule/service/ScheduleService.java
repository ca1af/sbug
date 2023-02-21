package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
import com.sparta.sbug.schedule.dto.PeriodRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    /**
     * 대상 유저의 일정을 등록
     *
     * @param requestDto 생성할 일정 정보가 담긴 DTO
     * @param user       대상 유저
     */
    void registerSchedule(ScheduleRequestDto requestDto, User user);

    /**
     * 대상 일정을 수정
     *
     * @param requestDto 수정할 정보가 담긴 DTO
     * @param scheduleId 수정할 일정 ID
     * @param userId     요청자 ID
     */
    void updateSchedule(
            ScheduleRequestDto requestDto,
            Long scheduleId,
            Long userId
    );

    void updateScheduleContent(
            String content,
            Long scheduleId,
            Long userId
    );

    void updateScheduleDate(
            LocalDateTime date,
            Long scheduleId,
            Long userId
    );

    void updateScheduleStatusToDone(Long id, Long userId);

    /**
     * 대상 일정을 삭제
     *
     * @param scheduleId 대상 일정 ID
     * @param userId     요청자 ID
     */
    void deleteSchedule(Long scheduleId, Long userId);

    /**
     * 요청자의 일정을 조회
     *
     * @param pageable 페이징 정보
     * @param user     요청자
     * @return Page&lt;ScheduleResponseDto&gt;
     */
    Page<ScheduleResponseDto> getMySchedules(Pageable pageable, User user);

    /**
     * 대상 일정의 정보를 조회
     *
     * @param scheduleId 대상 일정 ID
     * @return ScheduleResponseDto
     */
    ScheduleResponseDto getSchedule(Long scheduleId);

    /**
     * 특정 기간 내 요청자의 일정을 조회
     *
     * @param pageable  페이징 정보
     * @param user      요청자
     * @param periodDto 기간
     * @return Page&lt;ScheduleResponseDto&gt;
     */
    Page<ScheduleResponseDto> getPeriodSchedules(
            Pageable pageable,
            User user,
            PeriodRequestDto periodDto
    );

    List<ScheduleResponseDto> getSchedulesThisMonth(int year, int month, User user);
}
