package com.sparta.sbug.chat.repository;

import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRepositoryQuery {

    /**
     * 채팅방에 보내진 메세지들을 조회하는 메서드
     * Page로 반환하기 위해 데이터를 불러오는 메서드와 함께 전체 데이터 개수를 세는 쿼리가 한 번 더 실행됩니다.
     *
     * @param roomId 채팅방 ID
     * @param pageable 페이저블
     * @return Page&lt;Chat&gt;
     */
    Page<Chat> findMessagesInChatRoom(Long roomId, Pageable pageable);

    /**
     * 특정 유저에게 온 특정 상태의 메세지들의 개수를 구하는 메서드
     *
     * @param id     유저(수신자) ID
     * @param status 상태
     * @return Long
     */
    Long countByReceiverIdAndStatus(Long id, ChatStatus status);

}
