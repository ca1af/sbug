package com.sparta.sbug.chat.dto;

import com.sparta.sbug.chat.entity.Chat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 채팅 응답 DTO
 */
// lombok
@Getter
@RequiredArgsConstructor
public class ChatResponseDto {
    private Long id;
    private String sender;
    private String receiver;
    private Long receiverId;
    private String message;
    private String status;
    private LocalDateTime createdAt;

    /**
     * 생성자
     */
    private ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.sender = chat.getSender().getNickname();
        this.message = chat.getMessage();
        this.receiver = chat.getReceiver().getNickname();
        this.receiverId = chat.getReceiver().getId();
        this.status = chat.getStatus().toString();
        this.createdAt = chat.getCreatedAt();
    }

    public static ChatResponseDto of(Chat chat){
        return new ChatResponseDto(chat);
    }
}
