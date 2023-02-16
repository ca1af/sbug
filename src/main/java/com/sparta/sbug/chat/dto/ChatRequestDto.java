package com.sparta.sbug.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 메세지 요청 DTO
 */
// lombok
@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
    private Long roomId;
    private Long receiverId;
    private String message;
}
