package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ChatService {


    /**
     * 채팅방 아이디로 주고 받은 메세지들을 모두 조회하는 메서드
     * 이 메서드는 조회된 메세지의 수신자가 요청자인 메세지들을 읽음 상태로 변경하는 메서드를 내부에서 호출하고 있습니다.
     *
     * @param myUserId 내 아이디
     * @param roomId   채팅방 아이디
     * @return List&lt;ChatResponseDto&gt;
     */
    Slice<ChatResponseDto> readAllMessageInChatRoom(Long myUserId, Long roomId, PageDto pageDto);

    /**
     * 메세지를 생성하는 메서드
     *
     * @param sender   나(송신자)
     * @param receiver 상대방(수신자)
     * @param message  메세지 내용
     * @return ChatResponseDto
     */
    ChatResponseDto createMessage(ChatRoom chatRoom, User sender, User receiver, String message);

    /**
     * 채팅 중인 채팅방의 아래와 같은 정보들을 조회합니다.
     * 1. 채팅방 ID
     * 2. 채팅 상대방 ID
     * 3. 채팅방에 아직 읽지 않은 메세지 개수
     *
     * @param user 나(수신자)
     * @return List&lt;NewMessageCountDto&gt;
     */
    @Transactional(readOnly = true)
    Map<Long, Long> countNewMessageByChatRoom(User user);

    /**
     * 요청자가 아직 읽지 않은 메세지의 개수를 찾는 메서드
     *
     * @param user 요청자
     * @return Long
     */
    Long countNewMessages(User user);
}
