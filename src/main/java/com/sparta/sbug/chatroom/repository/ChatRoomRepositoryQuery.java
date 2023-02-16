package com.sparta.sbug.chatroom.repository;

public interface ChatRoomRepositoryQuery {

    /**
     * 두 유저가 속해있는 채팅방 ID를 찾아 반환합니다.
     *
     * @param userId1 유저 1
     * @param userId2 유저 2
     * @return Long
     */
    Long findChatRoomIdByUsers(Long userId1, Long userId2);

}
