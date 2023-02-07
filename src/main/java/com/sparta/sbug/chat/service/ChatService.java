package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;

import java.util.List;

public interface ChatService {


    /**
     * 주고 받은 메세지를 조회하는 메서드
     *
     * @param myUserId       : 내 아이디
     * @param theOtherUserId : 상대방 아이디
     */
    List<ChatResponseDto> getAllExchangedMessage(Long myUserId, Long theOtherUserId);

    /**
     * 메세지를 전송하는 메서드
     *
     * @param receiverId
     * @param message
     */
    void sendMessage(Long receiverId, String message);

    /**
     * 메세지를 수정하는 메서드
     *
     * @param messageId
     * @param message
     */
    void updateMessage(Long messageId, String message);

    /**
     * 메세지를 삭제하는 메서드
     *
     * @param messageId
     */
    void deleteMessage(Long messageId);

}
