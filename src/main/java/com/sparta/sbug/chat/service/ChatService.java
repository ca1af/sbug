package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;

import java.util.List;

public interface ChatService {

    /**
     * 해당 채팅방의 메세지를 모두 찾아 반환하는 메소드
     * @param chatRoomId
     */
    List<ChatResponseDto> getAllMessageInChatRoom(Long chatRoomId);


    /**
     * 메세지를 작성하는 메서드
     * @param chatRoomId
     * @param message
     */
    void sendMessage(Long chatRoomId, String message);

    /**
     * 메세지를 수정하는 메서드
     * @param messageId
     * @param message
     */
    void updateMessage(Long messageId, String message);

    /**
     * 메세지를 삭제하는 메서드
     * @param id
     */
    void deleteMessage(Long id);

}
