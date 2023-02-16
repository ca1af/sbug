package com.sparta.sbug.thread.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쓰레드 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class ThreadRequestDto {
    @NotNull(message = "내용을 입력해주세요.")
    String content;
}
