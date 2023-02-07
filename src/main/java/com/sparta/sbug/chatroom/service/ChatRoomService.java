package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.chatroom.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatRoomService {
    /**
     * 내 사용자 ID를 이용하여, 내가 속한 모든 채팅방을 찾아서 반환
     * @param userId
     */
    List<ChatRoomResponseDto> getAllMyChatRoom(Long userId);

    /**
     * 채팅 방의 아이디를 찾는 메소드, 존재하지 않으면 음수를 반환
     * @param myUserId 내 사용자 아이디
     * @param theOtherUserId 상대방 아이디
     */
    Long getChatRoomIdOrNegative(Long myUserId, Long theOtherUserId);

}
