package com.sparta.sbug.chatroom.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
public class ChatRoomDto {
    private final Long roomId;
    private final String email;
    private final String nickname;

    @Setter
    private String profileImage;

    public ChatRoomDto(Long roomId, String email, String nickname, String profileImage) {
        this.roomId = roomId;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
