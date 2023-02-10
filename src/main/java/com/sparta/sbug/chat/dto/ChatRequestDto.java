package com.sparta.sbug.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@Getter
@NoArgsConstructor
public class ChatRequestDto {
    private Long roomId;
    private String message;
}
