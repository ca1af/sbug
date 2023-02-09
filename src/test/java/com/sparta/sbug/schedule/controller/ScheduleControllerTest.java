package com.sparta.sbug.schedule.controller;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.schedule.service.ScheduleService;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
import com.sparta.sbug.schedule.dto.PeriodRequestDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.time.LocalDateTime;

public class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @Test
    @DisplayName("registerSchedule() Controller Test")
    void registerSchedule() {

        //given
        ScheduleRequestDto request = new ScheduleRequestDto(
            "팀 최종 프로젝트 미팅",
            LocalDateTime.now()
        );

        //when scheduleController.registerSchedule()이 호출되었을 때..
        String redirect = scheduleController.registerSchedule(
            request,
            any(UserDetailsImpl.class)
        );

        //then

        //scheduleService.registerSchedule()이 한번 호출되는가?
        verify(scheduleService, times(1)).registerSchedule(
            request,
            any(User.class)
        );

        //redirect url이 제대로 반환되는가?
        assertThat(redirect).isEqualTo("/user/schedule");
    }
    
    @Test
    @DisplayName("updateSchedule() Controller Test")
    void updateSchedule() {

        //given
        ScheduleRequestDto request = new ScheduleRequestDto(
            "팀 최종 프로젝트 미팅",
            LocalDateTime.now()
        );
        long scheduleId = 1L;
        long userId = 39201L;

        //when
        String redirect = scheduleController.updateSchedule(
            request,
            scheduleId,
            any(UserDetailsImpl.class)
        );

        //then

        //scheduleService.updateSchedule()이 한번 호출되는가?
        verify(scheduleService, times(1)).updateSchedule(
            request,
            scheduleId,
            userId
        );

        //redircet url이 제대로 반환되는가?
        assertThat(redirect).isEqualTo("/user/schedule");
    }

    @Test
    @DisplayName("deleteSchedule() Controller Test")
    void deleteSchedule() {

        //given 
        long scheduleId = 1L;
        long userId = 2323L;

        //when
        String redirect = scheduleController.deleteSchedule(
            scheduleId,
            any(UserDetailsImpl.class)
        );

        //then

        //scheduleService.deleteSchedule()이 한번 호출되는가?
        verify(scheduleService, times(1)).deleteSchedule(
            scheduleId,
            userId
        );

        //redirect url이 제대로 반환되는가?
        assertThat(redirect).isEqualTo("/user/schedule");

    }

    /**
    @Test
    @DisplayName("getMySchedules() Controller Test")
    void getMySchedules() {

        //given
        Page<ScheduleResponseDto> pageStub
            = Page<ScheduleResponseDto>.empty();

        //when
        when(scheduleService.getMySchedules(
            any(Pageable.class),
            any(User.class)
        ))
        .thenReturn(pageStub);

        //then
    }
    */

    @Test
    @DisplayName("getSchedule() Controller Test")
    void getSchedule() {

        //given

        //when

        //then
    }

    @Test
    @DisplayName("getPeriodSchedules() Controller Test")
    void getPeriodSchedules() {

        //given

        //when

        //then
    }
}

