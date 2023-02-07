package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
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
public class ScheduleServiceImpl {

    private final ScheduleRepository scheduleRepository;
    /**

    @Override
    void registerSchedule(UpdateScheduleDto updateDto, User user) {
    }
    @Override
    void updateSchedule(Long scheduleId, UpdateScheduleDto updateDto, User user){
    }
    @Override
    void deleteSchedule(Long scheduleId, Long userId){
    }
    @Override
    @Transactional
    Page<ScheduleResponseDto> getMySchdules(Pageable pageable, User user){
    }
    */
}
