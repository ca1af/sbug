package com.sparta.sbug.chat.repository;

import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRepositoryQuery {

    Page<Chat> findExchangedMessages(Long userId1, Long userId2, Pageable pageable);

    Long countByReceiverIdAndStatus(Long id, ChatStatus status, Pageable pageable);

}
