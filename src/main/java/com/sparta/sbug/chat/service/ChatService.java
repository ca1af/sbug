package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatService {


    /**
     * 채팅방 아이디로 주고 받은 메세지들을 모두 조회하는 메서드
     * 이 메서드는 조회된 메세지의 수신자가 요청자인 메세지들을 읽음 상태로 변경하는 메서드를 내부에서 호출하고 있습니다.
     *
     * @param myUserId 내 아이디
     * @param roomId   채팅방 아이디
     * @return List&lt;ChatResponseDto&gt;
     */
    List<ChatResponseDto> readAllMessageInChatRoom(Long myUserId, Long roomId, PageDto pageDto);

    /**
     * 조회된 메세지의 수신자가 요청자인 메세지들을 읽음 상태로 변경하는 메서드
     *
     * @param requesterId 요청자의 ID
     * @param chats       조호된 메세지 리스트
     */
    void convertToRead(Long requesterId, Page<Chat> chats);

    /**
     * 엔터티들을 DTO로 옮겨 담는 메서드
     *
     * @param pageChats 메세지 엔터티 리스트
     * @return List&lt;ChatResponseDto&gt;
     */
    List<ChatResponseDto> getDtoListFromEntities(Page<Chat> pageChats);

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
     * 메세지를 수정하는 메서드
     *
     * @param messageId 대상 메세지 ID
     * @param user      요청자
     * @param message   수정할 메세지 내용
     */
    void updateMessage(Long messageId, User user, String message);

    /**
     * 메세지를 삭제하는 메서드
     *
     * @param messageId 대상 메세지 ID
     * @param user      요청자
     */
    void deleteMessage(Long messageId, User user);

    /**
     * 메세지 ID로 메세지를 찾는 메서드
     *
     * @param messageId 메세지 ID
     * @return Chat
     */
    Chat findChatById(Long messageId);

    /**
     * 채팅의 소유자가 요청자인지 확인하는 메서드
     *
     * @param chat 검증할채팅
     * @param user 요청자
     */
    void validateUserIsSender(Chat chat, User user);

    /**
     * 요청자가 아직 읽지 않은 메세지의 개수를 찾는 메서드
     *
     * @param user 요청자
     * @return Long
     */
    Long countNewMessages(User user);
}
