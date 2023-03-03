package com.sparta.sbug.chat.repository.query;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface ChatRepositoryQuery {

    /**
     * 채팅방에 보내진 메세지들을 조회하는 메서드
     * Page로 반환하기 위해 데이터를 불러오는 메서드와 함께 전체 데이터 개수를 세는 쿼리가 한 번 더 실행됩니다.
     *
     * @param roomId   채팅방 ID
     * @param pageable 페이저블
     * @return Page&lt;Chat&gt;
     */
    Slice<ChatResponseDto> findMessagesInChatRoom(Long roomId, Pageable pageable);

    /**
     * 읽은 메세지들을 읽음 상태로 변경
     *
     * @param chatIds 대상 메세지들
     */
    void convertMessagesStatusToRead(List<Long> chatIds);

    /**
     * 채팅 중인 채팅방의 아래와 같은 정보들을 조회합니다.
     * 1. 채팅방 ID
     * 2. 채팅 상대방 ID
     * 3. 채팅방에 아직 읽지 않은 메세지 개수
     *
     * @param userId 나(수신자)
     * @return List&lt;NewMessageCountDto&gt;
     */
    Map<Long, Long> countNewMessageByChatRoom(Long userId);

    /**
     * 특정 유저에게 온 특정 상태의 메세지들의 개수를 구하는 메서드
     *
     * @param id 유저(수신자) ID
     * @return Long
     */
    Long countNewMessageByReceiverId(Long id);

}
