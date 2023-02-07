package com.sparta.sbug.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// lombok
@Getter
@RequiredArgsConstructor
public class ChatResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String message;

    /**
     * 생성자
     */
    @Builder
    public ChatResponseDto(Long id, String email, String nickname, String message) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.message = message;
    }
}
