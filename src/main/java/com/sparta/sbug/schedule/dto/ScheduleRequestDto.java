package com.sparta.sbug.schedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 일정 등록 or 수정 요청 DTO
 */
// lombok
@Getter
@RequiredArgsConstructor
public class ScheduleRequestDto {

    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime date;
}
