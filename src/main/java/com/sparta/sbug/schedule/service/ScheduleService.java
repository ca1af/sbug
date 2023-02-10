package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.repository.ScheduleRepository;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
import com.sparta.sbug.schedule.dto.PeriodRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ScheduleService {
    //일정 등록
    void registerSchedule(ScheduleRequestDto requestDto, User user);
    //일정 수정
    void updateSchedule(
        ScheduleRequestDto requestDto,
        Long scheduleId,
        Long userId
    );
    //일정 삭제
    void deleteSchedule(Long scheduleId, Long userId);
    //내 일정 조회
    Page<ScheduleResponseDto> getMySchedules(Pageable pageable, User user);
    //일정 상세 조회
    ScheduleResponseDto getSchedule(Long scheduleId);
    //기간내 일정 조회
    Page<ScheduleResponseDto> getPeriodSchedules(
        Pageable pageable,
        User user,
        PeriodRequestDto periodDto
    );

}
