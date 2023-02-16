package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.user.entity.User;

public interface ChatRoomService {

    /**
     * 두 유저 간에 채팅방을 생성하는 메서드
     *
     * @param user1 유저 1
     * @param user2 유저 2
     * @return Long : 채팅방 ID
     */
    Long createChatRoom(User user1, User user2);

    /**
     * 두 유저 간에 채팅방 ID를 찾는 메서드
     * 채팅 방이 존재하지 않으면 음수를 반환
     *
     * @param user1 유저 1
     * @param user2 유저 2
     * @return Long : 채팅방 ID or 음수(-1)
     */
    Long getChatRoomIdOrNegative(User user1, User user2);

    /**
     * 채팅방 ID로 채팅방 엔티티를 찾는 메서드
     *
     * @param id 채팅방 ID
     * @return ChatRoom
     */
    ChatRoom getChatRoomById(Long id);
}
