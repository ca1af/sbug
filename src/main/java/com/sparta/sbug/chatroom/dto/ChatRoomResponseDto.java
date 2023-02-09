package com.sparta.sbug.chatroom.dto;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

// lombok
@Getter
@RequiredArgsConstructor
public class ChatRoomResponseDto {
    private Long roomId;
    private List<ChatResponseDto> chats;
}
