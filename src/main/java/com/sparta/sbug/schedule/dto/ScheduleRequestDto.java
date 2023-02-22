package com.sparta.sbug.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 일정 등록 or 수정 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class ScheduleRequestDto {

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Getter
    @NoArgsConstructor
    public static class ContentUpdate {
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class DateUpdate {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime date;
    }
}
