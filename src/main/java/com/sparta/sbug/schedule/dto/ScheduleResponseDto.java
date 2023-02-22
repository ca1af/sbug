package com.sparta.sbug.schedule.dto;

import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.entity.ScheduleStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 일정 응답 DTO
 */
// lombok
@Getter
@RequiredArgsConstructor
public class ScheduleResponseDto {

    private final Long scheduleId;

    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime date;

    private final ScheduleStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime doneAt;

    /**
     * 생성자
     */
    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.content = schedule.getContent();
        this.date = schedule.getDate();
        this.status = schedule.getStatus();
        this.doneAt = schedule.getDoneAt();
    }

    /**
     * 일정 엔티티들을 DTO에 담아 반환하는 메서드
     *
     * @param mySchedules 일정 엔티티
     * @return Page&lt;ScheduleResponseDto&gt;
     */
    public static Page<ScheduleResponseDto> toDtoList(
            Page<Schedule> mySchedules
    ) {
        return mySchedules.map(ScheduleResponseDto::of);
    }

    public static ScheduleResponseDto of(Schedule schedule){
        return new ScheduleResponseDto(schedule);
    }
}
