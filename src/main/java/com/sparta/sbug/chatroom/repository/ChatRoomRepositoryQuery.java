package com.sparta.sbug.chatroom.repository;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatRoomRepositoryQuery {

    Page<ChatRoom> findAllByUserId(Long userId, Pageable pageable);

    Optional<ChatRoom> findByUsersId(Long myUserId, Long theOtherUserId);
}
