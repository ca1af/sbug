package com.sparta.sbug.chat.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NewMessageCountDto {
    private Long chatRoomId;
    private Long newMessageCount;

    public NewMessageCountDto(Long chatRoomId, Long newMessageCount) {
        this.chatRoomId = chatRoomId;
        this.newMessageCount = newMessageCount;
    }
}
