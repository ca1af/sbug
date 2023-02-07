package com.sparta.sbug.chatroom.repository;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomSearchCondition {
    private Long chatRoomId;
    private Long userId;
    private Long theOtherUserId;
}
