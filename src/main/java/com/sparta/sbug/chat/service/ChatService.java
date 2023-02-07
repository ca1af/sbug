package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface ChatService {


    /**
     * 주고 받은 메세지를 조회하는 메서드
     *
     * @param myUserId       : 내 아이디
     * @param theOtherUserId : 상대방 아이디
     */
    List<ChatResponseDto> readAllExchangedMessage(Long myUserId, Long theOtherUserId, PageDto pageDto);

    /**
     * 메세지를 전송하는 메서드
     *
     * @param sender   : 나(송신자)
     * @param receiver : 상대방(수신자)
     * @param message  : 메세지 내용
     */
    String sendMessage(User sender, User receiver, String message);

    /**
     * 메세지를 수정하는 메서드
     *
     * @param messageId : 대상 메세지 ID
     * @param message   : 요청자
     * @param message   : 수정할 메세지 내용
     */
    String updateMessage(Long messageId, User user, String message);

    /**
     * 메세지를 삭제하는 메서드
     *
     * @param messageId : 대상 메세지 ID
     * @param user      : 요청자
     */
    String deleteMessage(Long messageId, User user);

    /**
     * 읽지 않은 메세지를 찾는 메서드
     */
    Long countNewMessages(User user, PageDto pageDto);
}
