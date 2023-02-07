package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.repository.ScheduleRepository;
import com.sparta.sbug.schedule.dto.UpdateScheduleDto;
import com.sparta.sbug.schedule.dto.ScheduleResponseDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    //일정 등록
    @Override
    public void registerSchedule(UpdateScheduleDto updateDto, User user) {
        Schedule newSchedule = Schedule.builder()
            .user(user)
            .content(updateDto.getContent())
            .date(updateDto.getDate())
            .build();
        scheduleRepository.save(newSchedule);
    }

    //일정 수정
    @Override
    public void updateSchedule(Long scheduleId, UpdateScheduleDto updateDto, Long userId){
        Schedule foundSchedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new IllegalStateException("일정을 찾을 수 없습니다.")
        );
        if (foundSchedule.getUser().getId() == userId) {
            foundSchedule.updateSchedule(updateDto.getContent(), updateDto.getDate());
            scheduleRepository.save(foundSchedule);
        }
    }

    //일정 삭제
    @Override
    public void deleteSchedule(Long scheduleId, Long userId){
        Schedule foundSchedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new IllegalStateException("일정을 찾을 수 없습니다.")
        );
        if (foundSchedule.getUser().getId() == userId) {
            scheduleRepository.delete(foundSchedule);
        }

    }

    //내 일정 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> getMySchedules(Pageable pageable, User user){
        Page<Schedule> mySchedules = scheduleRepository.findAllByUserId(user.getId(), pageable);
        Page<ScheduleResponseDto> responseDtoList = ScheduleResponseDto.toDtoList(mySchedules);
        return responseDtoList;
    }

    //일정 상세 조회
    @Override
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule foundSchedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new IllegalStateException("일정을 찾을 수 없습니다.")
        );
        ScheduleResponseDto responseDto = new ScheduleResponseDto(foundSchedule);
        return responseDto;
    }
}











