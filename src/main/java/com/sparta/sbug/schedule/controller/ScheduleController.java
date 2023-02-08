package com.sparta.sbug.schedule.controller;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.service.ScheduleService;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
import com.sparta.sbug.schedule.dto.PeriodRequestDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    //일정 등록
    @PostMapping("/user/schedule")
    public String registerSchedule(@RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scheduleService.registerSchedule(requestDto, userDetails.getUser());
        return "/user/schedule";
    }

    //일정 수정
    @PutMapping("/user/schedule/{id}")
    public String updateSchedule(@RequestBody ScheduleRequestDto requestDto, @PathVariable Long scheduleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        scheduleService.updateSchedule(requestDto, scheduleId, userId);
        return "/user/schedule";
    }

    //일정 삭제
    @DeleteMapping("/user/schedule/{id}")
    public String deleteSchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        scheduleService.deleteSchedule(scheduleId, userId);
        return "/user/schedule";
    }

    //내 일정 조회 
    @GetMapping("/user/schedule")
    public Page<ScheduleResponseDto> getMySchedules(Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return scheduleService.getMySchedules(pageable, user);
    }

    //일정 상세 조회
    @GetMapping("/user/schedule/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long scheduleId) {
        return scheduleService.getSchedule(scheduleId);
    }
    //기간내 일정 조회
    @GetMapping("/user/schedule/period")
    public Page<ScheduleResponseDto> getPeriodSchedules(
    Pageable pageable,
    @RequestBody PeriodRequestDto periodDto,
    @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        return scheduleService.getPeriodSchedules(pageable, user, periodDto);
    }
}
