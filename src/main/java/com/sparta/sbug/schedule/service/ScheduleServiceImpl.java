package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.entity.ScheduleStatus;
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
import java.time.temporal.TemporalAdjusters;
import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service

// springframework transaction
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
                .status(ScheduleStatus.UNDONE)
                .build();
        scheduleRepository.save(newSchedule);
    }

    //일정 수정(내용, 날짜)
    @Override
    public void updateSchedule(
            ScheduleRequestDto requestDto,
            Long scheduleId,
            Long userId
    ) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.updateSchedule(
                requestDto.getContent(),
                requestDto.getDate()
        );
        scheduleRepository.save(foundSchedule);
    }

    //일정 완료 표시(status 변경)
    @Override
    public void completeSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.checkDoneSchedule();
        scheduleRepository.save(foundSchedule);
    }

    //일정 미완 표시(status 변경)
    @Override
    public void incompleteSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.uncheckDoneSchedule();
        scheduleRepository.save(foundSchedule);
    }

    //StudyMode 켜기 
    @Override
    public void turnOnStudyMode(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.studyModeOn();
        scheduleRepository.save(foundSchedule);
    }

    //StudyMode 끄기
    @Override
    public void turnOffStudyMode(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.studyModeOff();
        scheduleRepository.save(foundSchedule);
    }


    @Override
    public void updateScheduleContent(String content, Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.setContent(content);
    }

    @Override
    public void updateScheduleDate(LocalDateTime date, Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.setDate(date);
    }

    //일정 삭제
    @Override
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        scheduleRepository.delete(foundSchedule);
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
        return ScheduleResponseDto.toDtoList(mySchedules);
    }

    //일정 상세 조회
    @Override
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        return new ScheduleResponseDto(foundSchedule);
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
        return ScheduleResponseDto.toDtoList(periodSchedules);
    }

    //내 특정 월 일정 조회
    @Override
    public List<ScheduleResponseDto> getSchedulesThisMonth(int year, int month, User user) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        List<Schedule> periodSchedules =
                scheduleRepository.findAllByUserIdAndDateBetween(
                        user.getId(), startDate, endDate
                );
        return periodSchedules.stream().map(ScheduleResponseDto::of).toList();
    }

    // 일정 조회
    public Schedule validateSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정을 찾을 수 없습니다.")
        );
    }

    public void validateRequester(Long requesterId, Long scheduleId) {
        if (!requesterId.equals(scheduleId)) {
            throw new IllegalStateException("요청자에게 권한이 없습니다.");
        }
    }

}











