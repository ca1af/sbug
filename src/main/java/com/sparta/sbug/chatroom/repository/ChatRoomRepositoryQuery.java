package com.sparta.sbug.chatroom.repository;

import com.sparta.sbug.chat.entity.ChatStatus;

public interface ChatRoomRepositoryQuery {

    Long findChatRoomIdByUsers(Long userId1, Long userId2);

}
