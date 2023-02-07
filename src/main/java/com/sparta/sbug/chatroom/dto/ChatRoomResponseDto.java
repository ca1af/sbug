package com.sparta.sbug.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private String chatRoomName;

    @Builder
    public ChatRoomResponseDto(Long id, String chatRoomName) {
        this.id = id;
        this.chatRoomName = chatRoomName;
    }
}
