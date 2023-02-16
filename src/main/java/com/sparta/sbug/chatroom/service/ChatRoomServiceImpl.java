package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.chatroom.repository.ChatRoomRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service

// springframework transaction
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Long createChatRoom(User user1, User user2) {
        ChatRoom chatRoom = ChatRoom.builder().user1(user1).user2(user2).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    @Override
    public Long getChatRoomIdOrNegative(User user1, User user2) {
        return chatRoomRepository.findChatRoomIdByUsers(user1.getId(), user2.getId());
    }

    @Override
    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow();
    }
}