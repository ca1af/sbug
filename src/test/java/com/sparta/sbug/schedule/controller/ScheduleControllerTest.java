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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    @Test
    @DisplayName("getMySchedules() Controller Test")
    void getMySchedules() {

        //given
        Pageable pageable = PageRequest.of(3, 3);
        User user = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();
        UserDetailsImpl userDetailsImpl =
            new UserDetailsImpl(user, "123");

        //비어있는 변수 result를 만든다
        Optional<Integer> result = Optional.empty();

        //Mock객체 scheduleService의 method는
        //result를 반환하도록 설정
        //doReturn은 non-type safe라서 result의 type을
        //임의로 정할 수 있다.
        doReturn(result).when(scheduleService).getMySchedules(
            any(Pageable.class),
            any(User.class)
        );
        
        //when

        //scheduleController의 method(Test대상)의 return은
        //response에 담는다.
        Page<ScheduleResponseDto> response
            = scheduleController.getMySchedules(
                pageable,
                userDetailsImpl
            );

        //then

        //response와 result의 주소가 같은지 확인
        //이게 된다고..?
        assertThat(response).isSameAs(result);

    }

    @Test
    @DisplayName("getSchedule() Controller Test")
    void getSchedule() {

        //given
        long scheduleId = 123L;

        User user  = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();

        Schedule schedule = Schedule.builder()
            .user(user)
            .content("123")
            .date(LocalDateTime.now())
            .build();

        ScheduleResponseDto resultStub = new ScheduleResponseDto(
            schedule
        );

        when(scheduleService.getSchedule(scheduleId))
            .thenReturn(resultStub);

        //when
        ScheduleResponseDto response
            = scheduleController.getSchedule(scheduleId);

        //then
        assertThat(response).isSameAs(resultStub);
    }

    @Test
    @DisplayName("getPeriodSchedules() Controller Test")
    void getPeriodSchedules() {

        //given
        Pageable pageable = PageRequest.of(3, 3);

        User user = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();

        UserDetailsImpl userDetailsImpl =
            new UserDetailsImpl(user, "123");

        PeriodRequestDto periodDto = new PeriodRequestDto(
            LocalDateTime.now(),
            LocalDateTime.now().plus(3, ChronoUnit.DAYS)
        );


        //비어있는 변수 result를 만든다
        Optional<Integer> result = Optional.empty();

        //Mock객체 scheduleService의 method는
        //result를 반환하도록 설정
        //doReturn은 non-type safe라서 result의 type을
        //임의로 정할 수 있다.
        doReturn(result).when(scheduleService).getPeriodSchedules(
            any(Pageable.class),
            any(User.class),
            any(PeriodRequestDto.class)
        );
        
        //when

        //scheduleController의 method(Test대상)의 return은
        //response에 담는다.
        Page<ScheduleResponseDto> response
            = scheduleController.getPeriodSchedules(
                pageable,
                periodDto,
                userDetailsImpl
            );

        //then

        //response와 result의 주소가 같은지 확인
        assertThat(response).isSameAs(result);



    }
}

