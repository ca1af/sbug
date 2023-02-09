package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.chat.repository.ChatRepository;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.chatroom.repository.ChatRoomRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Long createChatRoom(User user1, User user2) {
        ChatRoom chatRoom = ChatRoom.builder().user1(user1).user2(user2).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    public Long getChatRoomIdOrNegative(User user1, User user2) {
        return chatRoomRepository.findChatRoomIdByUsers(user1.getId(), user2.getId());
    }
}
