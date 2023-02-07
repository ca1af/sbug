package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.chatroom.repository.ChatRoomRepository;
import com.sparta.sbug.chatroom.repository.ChatRoomSearchCondition;
import com.sparta.sbug.common.dto.PageDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoom> getAllMyChatRoom(Long userId, PageDto pageDto) {
        ChatRoomSearchCondition cond = ChatRoomSearchCondition.builder().userId(userId).build();
        return chatRoomRepository.findAllByUserId(userId, pageDto.toPageable());
    }

    @Override
    public Long getChatRoomIdOrNegative(Long myUserId, Long theOtherUserId) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByUsersId(myUserId, theOtherUserId);
        if (optionalChatRoom.isEmpty()) {
            return -1L;
        }
        return optionalChatRoom.get().getId();
    }
}
