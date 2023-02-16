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

// lombok
@RequiredArgsConstructor

// springframework web bind
@RestController
@RequestMapping("/api/users/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * 새로운 일정을 등록하는 메서드
     * [POST] /api/users/schedules
     *
     * @param requestDto  요청 DTO (내용, 예정일)
     * @param userDetails 요청자 정보
     */
    @PostMapping("")
    public void registerSchedule(
            @RequestBody ScheduleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        scheduleService.registerSchedule(requestDto, userDetails.getUser());
    }

    /**
     * 대상 일정을 수정하는 메서드
     * [PUT] /api/users/schedules/{id}
     *
     * @param requestDto  요청 DTO (내용, 예정일)
     * @param id          대상 일정 ID
     * @param userDetails 요청자 정보
     */
    @PutMapping("/{id}")
    public void updateSchedule(
            @RequestBody ScheduleRequestDto requestDto,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        scheduleService.updateSchedule(requestDto, id, userId);
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

    /**
     * 대상 일정을 삭제하는 메서드
     * [DELETE] /api/users/schedules/{id}
     *
     * @param id          대상 일정 ID
     * @param userDetails 요청자 정보
     */
    @DeleteMapping("/{id}")
    public void deleteSchedule(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        scheduleService.deleteSchedule(id, userId);
    }

    /**
     * 내 일정들을 조회하는 메서드
     * [GET] /api/users/schedules
     *
     * @param pageable    페이징 정보
     * @param userDetails 요청자 정보
     * @return Page&lt;ScheduleResponseDto&gt;
     */
    @GetMapping("")
    public Page<ScheduleResponseDto> getMySchedules(
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        return scheduleService.getMySchedules(pageable, user);
    }

    /**
     * 대상 일정의 정보를 조회하는 메서드
     * [GET] /api/users/schedules/{id}
     *
     * @param id 대상 일정 ID
     * @return ScheduleResponseDto
     */
    @GetMapping("/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long id) {
        return scheduleService.getSchedule(id);
    }

    /**
     * 기간 내 일정들을 조회하는 메서드
     * [GET] /api/users/schedules/{id}/period
     *
     * @param pageable    페이징 정보
     * @param periodDto   검색 기간 DTO (시작 시간, 끝 시간)
     * @param userDetails 요청자 정보
     * @return Page&lt;ScheduleResponseDto&gt;
     */
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
