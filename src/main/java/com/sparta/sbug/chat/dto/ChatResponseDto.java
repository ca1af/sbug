package com.sparta.sbug.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// lombok
@Getter
@RequiredArgsConstructor
public class ChatResponseDto {
    private Long id;
    private String sender;
    private String receiver;
    private String message;
    private String status;

    /**
     * 생성자
     */
    @Builder
    public ChatResponseDto(Long id, String sender, String message, String receiver, String status) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.status = status;
    }
}
