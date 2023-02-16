package com.sparta.sbug.chatroom.dto;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 채팅방 응답 DTO
 */
// lombok
@Getter
@RequiredArgsConstructor
public class ChatRoomResponseDto {
    private Long roomId;
    private List<ChatResponseDto> chats;

    /**
     * 생성자
     *
     * @param roomId 채팅방 ID
     * @param chats  채팅 내역
     */
    public ChatRoomResponseDto(Long roomId, List<ChatResponseDto> chats) {
        this.roomId = roomId;
        this.chats = chats;
    }
}
