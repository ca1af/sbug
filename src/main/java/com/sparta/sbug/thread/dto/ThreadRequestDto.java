package com.sparta.sbug.thread.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThreadRequestDto {
    @NotNull(message = "내용을 입력해주세요.")
    String content;
}
