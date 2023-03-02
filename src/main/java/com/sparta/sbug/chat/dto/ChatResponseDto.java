package com.sparta.sbug.chat.dto;

import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
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
    private Long roomId;
    private String sender;
    private String receiver;
    private Long receiverId;
    private String message;
    private String status;
    private LocalDateTime createdAt;

    public ChatResponseDto(Long id, Long roomId, String sender, String receiver, Long receiverId, String message, ChatStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.receiver = receiver;
        this.receiverId = receiverId;
        this.message = message;
        this.status = status.toString();
        this.createdAt = createdAt;
    }

    /**
     * 생성자
     */
    private ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.roomId = chat.getRoom().getId();
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
