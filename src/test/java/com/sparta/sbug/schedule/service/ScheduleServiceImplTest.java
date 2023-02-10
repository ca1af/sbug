package com.sparta.sbug.schedule.service;

import com.sparta.sbug.schedule.repository.ScheduleRepository;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.entity.Schedule;

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

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleServiceImpl;

    private ScheduleRequestDto request;
    private User user;
    private long scheduleId;
    private long userId;

    @BeforeEach
    public void init() {

        request = new ScheduleRequestDto(
            "팀 최종 프로젝트 미팅",
            LocalDateTime.now()
        );

        user = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();

        scheduleId = 123L;
        userId = 123L;

    }

    @Test
    @DisplayName("ServiceImpl.registerSchedule Test")
    public void registerSchedule() {
        //given
        //호출 여부만 검사

        when(scheduleRepository.save(any(Schedule.class)))
            .thenReturn(any(Schedule.class));

        //when
        scheduleServiceImpl.registerSchedule(request, user);

        //then
        verify(scheduleRepository, times(1)).save(
            any(Schedule.class)
        );

    }

    @Test
    @DisplayName("ServiceImpl.updateSchedule Test")
    public void updateSchedule() {
        //given

        when(scheduleRepository.save(any(Schedule.class)))
            .thenReturn(any(Schedule.class));

        //when
        scheduleServiceImpl.updateSchedule(
            request,
            scheduleId,
            userId
        );

        //then
        verify(scheduleRepository, times(1)).save(
            any(Schedule.class)
        );
    }

    @Test
    @DisplayName("serviceImpl.deleteSchedule Test")
    public void deleteSchedule() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("serviceImpl.getMySchedules Test")
    public void getMySchedules() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("serviceImpl.getSchedule Test")
    public void getSchedule() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("serviceImpl.getPeriodSchedules Test")
    public void getPeriodSchedules() {
        //given

        //when

        //then

    }

}
