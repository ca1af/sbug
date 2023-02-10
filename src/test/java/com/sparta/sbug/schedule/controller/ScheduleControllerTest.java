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

        ScheduleRequestDto request = new ScheduleRequestDto(
            "팀 최종 프로젝트 미팅",
            LocalDateTime.now()
        );

        long scheduleId = 1L;
        long userId = 39201L;

        Pageable pageable = PageRequest.of(3, 3);

        User user = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();

        UserDetailsImpl userDetails =
            new UserDetailsImpl(user, "123");

        Schedule schedule = Schedule.builder()
            .user(user)
            .content("123")
            .date(LocalDateTime.now())
            .build();


        PeriodRequestDto periodDto = new PeriodRequestDto(
            LocalDateTime.now(),
            LocalDateTime.now().plus(3, ChronoUnit.DAYS)
        );
    }

    @Test
    @DisplayName("registerSchedule() Controller Test")
    void registerSchedule() {

        //given
        doNothing().when(scheduleService).registerSchedule(
            any(ScheduleRequestDto.class),
            any(User.class)
        );

        //when scheduleController.registerSchedule()이 호출되었을 때..
        String redirect = scheduleController.registerSchedule(
            request,
            userDetails
        )
        //then

        //scheduleService.registerSchedule()이 한번 호출되는가?
        verify(scheduleService, times(1)).registerSchedule(
            any(ScheduleRequestDto.class),
            any(User.class)
        );

        //redirect url이 제대로 반환되는가?
        assertThat(redirect).isEqualTo("/user/schedule");
    }
    
    @Test
    @DisplayName("updateSchedule() Controller Test")
    void updateSchedule() {

        //given

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
                userDetails
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

