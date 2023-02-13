package com.sparta.sbug.schedule.controller;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.service.ScheduleService;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
import com.sparta.sbug.schedule.dto.PeriodRequestDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private ScheduleRequestDto request;
    private long scheduleId;
    private long userId;
    private Pageable pageable;
    private User user;
    private UserDetailsImpl userDetails;
    private Schedule schedule;
    private PeriodRequestDto periodDto;

    @BeforeEach
    public void init() {

        request = new ScheduleRequestDto(
            "팀 최종 프로젝트 미팅",
            LocalDateTime.now()
        );

        scheduleId = 1L;
        userId = 39201L;

        pageable = PageRequest.of(3, 3);

        user = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();

        userDetails =
            new UserDetailsImpl(user, "123");

        schedule = Schedule.builder()
            .user(user)
            .content("123")
            .date(LocalDateTime.now())
            .build();


        periodDto = new PeriodRequestDto(
            LocalDateTime.now(),
            LocalDateTime.now().plus(3, ChronoUnit.DAYS)
        );
    }

    //일정 등록
    @Test
    @DisplayName("@PostMapping('') schedule() Test: registerSchedule")
    public void schedule() {

    }
    
    //일정 수정
    @Test
    @DisplayName("@PutMapping('/{id}') schedule() Test: updateSchedule")
    public void schedule() {

    }

    //일정 삭제
    @Test
    @DisplayName("@DeleteMapping('/{id}') schedule() Test: deleteSchedule")
    public void deleteSchedule() {

    }

    //내 일정 조회
    @Test
    @DisplayName("@GetMapping('') mySchedules() Test: getMySchedules")
    public void getMySchedules() {

    }

    //일정 상세 조회
    @Test
    @DisplayName("@GetMapping('/{id}') schedule() Test: getSchedule")
    public void getSchedule() {

    }

    //기간내 일정 조회
    @Test
    @DisplayName("@GetMapping('/period') schedule() Test: getPeriodSchedule")
    public void getPeriodSchedules() {

    }
}

