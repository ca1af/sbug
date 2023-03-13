package com.sparta.sbug.schedule.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.entity.ScheduleStatus;
import com.sparta.sbug.schedule.entity.ScheduleMode;
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
import java.util.ArrayList;

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
                .mode(ScheduleMode.NORMAL)
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

    //일정 완료 표시(status 변경)
    //STUDYPLAN mode일 경우, generateReview 호출
    @Override
    public void completeSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        User requester = foundSchedule.getUser();
        validateRequester(requester.getId(), userId);
        foundSchedule.checkDoneSchedule();

        if (foundSchedule.getMode() == ScheduleMode.STUDYPLAN) {
            generateReview(foundSchedule, requester, foundSchedule.getDoneAt().plusDays(1));
            generateReview(foundSchedule, requester, foundSchedule.getDoneAt().plusWeeks(1));
            generateReview(foundSchedule, requester, foundSchedule.getDoneAt().plusMonths(1));
        }

        scheduleRepository.save(foundSchedule);
    }

    //일정 미완 표시(status 변경)
    //reviewIdList가 있을 경우, reviewIdList에 속한 Review들을 삭제
    @Override
    public void incompleteSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);

        foundSchedule.uncheckDoneSchedule();
        List<Long> reviewIdList = foundSchedule.getReviewIdList();

        if (reviewIdList != null) {
            foundSchedule.getReviewIdList().stream().forEach(scheduleRepository::deleteById);
        }

        foundSchedule.setReviewIdList(new ArrayList<Long>());

        scheduleRepository.save(foundSchedule);
    }

    //buildReview 호출하여 새 review 일정 build하고 repository에 save
    //새 review의 Id를 schedule의 reviewIdList에 추가
    public void generateReview(Schedule schedule, User user, LocalDateTime date) {
        String reviewContent = "[Review] : " + System.lineSeparator() + schedule.getContent();

        Schedule savedReview = scheduleRepository.save(
            buildReview(user, reviewContent, date)
        );

        List<Long> reviewIdList = schedule.getReviewIdList();
        reviewIdList.add(savedReview.getId());
        schedule.setReviewIdList(reviewIdList);
    }

    public Schedule buildReview(User user, String content, LocalDateTime date) {
        Schedule newReview = Schedule.builder()
            .user(user)
            .content(content)
            .date(date)
            .status(ScheduleStatus.UNDONE)
            .mode(ScheduleMode.REVIEW)
            .build();
        return newReview;
    }


    //StudyPlan Mode 설정 
    @Override
    public void turnOnStudyPlanMode(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.studyPlanModeOn();
        scheduleRepository.save(foundSchedule);
    }

    //Normal Mode로 되돌리기 
    @Override
    public void turnOffStudyPlanMode(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);
        foundSchedule.studyPlanModeOff();
        scheduleRepository.save(foundSchedule);
    }


    //일정 삭제
    @Override
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule foundSchedule = validateSchedule(scheduleId);
        validateRequester(foundSchedule.getUser().getId(), userId);

        List<Long> reviewIdList = foundSchedule.getReviewIdList();

        if (reviewIdList != null) {
            foundSchedule.getReviewIdList().stream().forEach(scheduleRepository::deleteById);
        }
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











