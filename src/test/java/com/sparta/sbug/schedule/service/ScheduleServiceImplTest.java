package com.sparta.sbug.schedule.service;

import com.sparta.sbug.schedule.repository.ScheduleRepository;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import static org.mockito.BDDMockito.given;
import org.mockito.BDDMockito.Then;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleServiceImpl;

    private ScheduleRequestDto request;
    private User user;
    private long userId;
    private Schedule schedule;
    private long scheduleId;
    private Schedule updatedSchedule;

    @BeforeEach
    public void init() {

        request = new ScheduleRequestDto(
            "팀 최종 프로젝트 미팅",
            LocalDateTime.of(2023, 5, 4, 12, 20)
        );

        user = User.builder()
            .email("123")
            .password("123")
            .nickname("123")
            .build();
        ReflectionTestUtils.setField(user, "id", 4878L);

        userId = user.getId();

        schedule = Schedule.builder()
            .user(user)
            .content("123")
            .date(LocalDateTime.of(2023, 5, 5, 14, 40))
            .build();
        ReflectionTestUtils.setField(schedule, "id", 4545L);

        scheduleId = schedule.getId();

    }

    @Test
    @DisplayName("ServiceImpl.registerSchedule Test")
    public void registerSchedule() {
        //given
        given(scheduleRepository.save(any(Schedule.class)))
            .willReturn(schedule);

        //when
        scheduleServiceImpl.registerSchedule(request, user);

        //then
        then(scheduleRepository).should(times(1)).save(any(Schedule.class));
    }

    @Test
    @DisplayName("ServiceImpl.updateSchedule Test")
    public void updateSchedule() {
        //given

        updatedSchedule = schedule;
        updatedSchedule.updateSchedule(
            request.getContent(),
            request.getDate()
        );

        given(scheduleRepository.findById(scheduleId))
            .willReturn(Optional.of(schedule));
        given(scheduleRepository.save(updatedSchedule))
            .willReturn(updatedSchedule);

        //when
        scheduleServiceImpl.updateSchedule(
            request,
            scheduleId,
            userId
        );

        //then
        then(scheduleRepository).should(times(1))
            .findById(eq(scheduleId));
        then(scheduleRepository).should(times(1))
            .save(eq(updatedSchedule));
    }

    @Test
    @DisplayName("serviceImpl.deleteSchedule Test")
    public void deleteSchedule() {
        //given
        given(scheduleRepository.findById(scheduleId))
            .willReturn(Optional.of(schedule));
        willDoNothing().given(scheduleRepository).delete(schedule);
        //when
        scheduleServiceImpl.deleteSchedule(
            scheduleId,
            userId
        );

        //then
        then(scheduleRepository).should(times(1))
            .findById(eq(scheduleId));
        then(scheduleRepository).should(times(1))
            .delete(schedule);

    }

    @Test
    @DisplayName("serviceImpl.getMySchedules Test")
    public void getMySchedules() {
        //given
        PageRequest pageable = PageRequest.of(0, 5);

        Schedule schedule1 = Schedule.builder()
            .user(user)
            .content("My First schedule")
            .date(LocalDateTime.of(2023, 5, 6, 14, 40))
            .build();
        ReflectionTestUtils.setField(schedule1, "id", 1111L);

        Schedule schedule2 = Schedule.builder()
            .user(user)
            .content("My Second schedule")
            .date(LocalDateTime.of(2023, 5, 7, 14, 40))
            .build();
        ReflectionTestUtils.setField(schedule2, "id", 2222L);

        Schedule schedule3 = Schedule.builder()
            .user(user)
            .content("My Third schedule")
            .date(LocalDateTime.of(2023, 5, 8, 14, 40))
            .build();
        ReflectionTestUtils.setField(schedule3, "id", 3333L);

        ArrayList scheduleList = new ArrayList();
        scheduleList.add(schedule1);
        scheduleList.add(schedule2);
        scheduleList.add(schedule3);

        Page<Schedule> expectedSchedules = new PageImpl(scheduleList);

        Page<ScheduleResponseDto> expectedDtoList =
            ScheduleResponseDto.toDtoList(expectedSchedules);

        given(scheduleRepository.findAllByUserId(userId, pageable))
            .willReturn(expectedSchedules);

        //when
        Page<ScheduleResponseDto> response =
            scheduleServiceImpl.getMySchedules(pageable, user);
        ScheduleResponseDto responseDto1 = response.getContent().get(0);
        ScheduleResponseDto responseDto2 = response.getContent().get(1);
        ScheduleResponseDto responseDto3 = response.getContent().get(2);
            

        //then
        then(scheduleRepository).should(times(1))
            .findAllByUserId(userId, pageable);

        assertThat(responseDto1.getScheduleId())
            .isEqualTo(schedule1.getId());
        assertThat(responseDto2.getScheduleId())
            .isEqualTo(schedule2.getId());
        assertThat(responseDto3.getScheduleId())
            .isEqualTo(schedule3.getId());




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
