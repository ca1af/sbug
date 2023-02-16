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
@RequestMapping("/api/users/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    //일정 등록
    @PostMapping("")
    public String registerSchedule(
        @RequestBody ScheduleRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        scheduleService.registerSchedule(requestDto, userDetails.getUser());
        return "/user/schedule";
    }

    //일정 수정
    @PutMapping("/{id}")
    public String updateSchedule(
        @RequestBody ScheduleRequestDto requestDto,
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        scheduleService.updateSchedule(requestDto, id, userId);
        return "/user/schedule";
    }

    //일정 완료 표시
    @PutMapping("/{id}/done")
    public String completeSchedule(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        scheduleService.completeSchedule(id, userId);
        return "/user/schedule";
    }

    //일정 미완 표시
    @PutMapping("/{id}/undone")
    public String incompleteSchedule(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        scheduleService.incompleteSchedule(id, userId);
        return "/user/schedule";
    }

    //일정 삭제
    @DeleteMapping("/{id}")
    public String deleteSchedule(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        scheduleService.deleteSchedule(id, userId);
        return "/user/schedule";
    }

    //내 일정 조회 
    @GetMapping("")
    public Page<ScheduleResponseDto> getMySchedules(
        Pageable pageable,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        return scheduleService.getMySchedules(pageable, user);
    }

    //일정 상세 조회
    @GetMapping("/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long id) {
        return scheduleService.getSchedule(id);
    }
    //기간내 일정 조회
    @GetMapping("/period")
    public Page<ScheduleResponseDto> getPeriodSchedules(
        Pageable pageable,
        @RequestBody PeriodRequestDto periodDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        return scheduleService.getPeriodSchedules(pageable, user, periodDto);
    }
}
