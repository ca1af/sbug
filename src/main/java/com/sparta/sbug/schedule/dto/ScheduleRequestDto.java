package com.sparta.sbug.schedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ScheduleRequestDto {
    private final String content;
    private final LocalDateTime date;
}