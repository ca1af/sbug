package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.repository.ScheduleRepository;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;
import com.sparta.sbug.schedule.dto.PeriodRequestDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    //일정 등록
    @Override
    public void registerSchedule(ScheduleRequestDto requestDto, User user) {
        Schedule newSchedule = Schedule.builder()
            .user(user)
            .content(requestDto.getContent())
            .date(requestDto.getDate())
            .build();
        scheduleRepository.save(newSchedule);
    }

    //일정 수정
    @Override
    public void updateSchedule(
        ScheduleRequestDto requestDto,
        Long scheduleId,
        Long userId
    ) {
        Schedule foundSchedule =
            scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정을 찾을 수 없습니다.")
            );
        if (userId.equals(foundSchedule.getUser().getId())) {
            foundSchedule.updateSchedule(
                requestDto.getContent(),
                requestDto.getDate()
            );
            scheduleRepository.save(foundSchedule);
        } else {
            throw new IllegalStateException("User id가 일치하지 않습니다.");
        }
    }

    //일정 삭제
    @Override
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule =
            scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정을 찾을 수 없습니다.")
            );
        if (userId.equals(foundSchedule.getUser().getId())) {
            scheduleRepository.delete(foundSchedule);
        } else {
            throw new IllegalStateException("User id가 일치하지 않습니다.");
        }

    }

    //내 일정 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> getMySchedules(
        Pageable pageable,
        User user
    ) {
        Page<Schedule> mySchedules =
            scheduleRepository.findAllByUserId(user.getId(), pageable);
        Page<ScheduleResponseDto> responseDtoList =
            ScheduleResponseDto.toDtoList(mySchedules);
        return responseDtoList;
    }


    //일정 상세 조회
    @Override
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule foundSchedule =
            scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정을 찾을 수 없습니다.")
            );
        ScheduleResponseDto responseDto =
            new ScheduleResponseDto(foundSchedule);
        return responseDto;
    }
    //기간내 일정 조회
    @Override
    public Page<ScheduleResponseDto> getPeriodSchedules(
        Pageable pageable,
        User user,
        PeriodRequestDto periodDto
    ) {
        LocalDateTime startDate = periodDto.getStartDate();
        LocalDateTime endDate = periodDto.getEndDate();
        Page<Schedule> periodSchedules =
            scheduleRepository.findAllByUserIdAndDateBetween(
                user.getId(), startDate, endDate, pageable
            );
        Page<ScheduleResponseDto> responseDtoList =
            ScheduleResponseDto.toDtoList(periodSchedules);
        return responseDtoList;
    }
}











