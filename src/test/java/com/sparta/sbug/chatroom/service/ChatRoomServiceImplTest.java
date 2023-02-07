package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.chatroom.repository.ChatRoomRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ChatRoomServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatRoomService chatRoomService;

    @Test
    @DisplayName("내 전체 채팅방 조회")
    void getAllMyChatRoom() {
        // given
        User user = User.builder().email("user1").password("hi").nickname("hello").build();
        user.setUserRole(UserRole.USER);
        User savedUser = userRepository.save(user);

        ChatRoom chatRoom1 = ChatRoom.builder().chatRoomName("chatRoom1").build();
        ChatRoom chatRoom2 = ChatRoom.builder().chatRoomName("chatRoom2").build();

        ChatRoom savedChatRoom1 = chatRoomRepository.save(chatRoom1);
        ChatRoom savedChatRoom2 = chatRoomRepository.save(chatRoom2);

        PageDto pageDto = PageDto.builder().currentPage(1).size(2).sortBy("createdAt").build();

        // when
        Page<ChatRoom> chatRooms = chatRoomService.getAllMyChatRoom(savedUser.getId(), pageDto);

        // then
        assert chatRooms.getContent().equals(List.of(savedChatRoom1, savedChatRoom2));
    }

    @Test
    @DisplayName("두 유저 아이디로 채팅방 찾기")
    void getChatRoomIdOrNegative() {
        // given
        User user1 = User.builder().email("user1").password("hi").nickname("hello").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("hi").nickname("hello").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        ChatRoom chatRoom1 = ChatRoom.builder().chatRoomName("chatRoom1").build();
        ChatRoom chatRoom2 = ChatRoom.builder().chatRoomName("chatRoom2").build();

        ChatRoom savedChatRoom1 = chatRoomRepository.save(chatRoom1);
        ChatRoom savedChatRoom2 = chatRoomRepository.save(chatRoom2);

        savedChatRoom1.addUser(savedUser1);
        savedChatRoom1.addUser(savedUser2);
        savedChatRoom2.addUser(savedUser1);

        // when
        Long roomId = chatRoomService.getChatRoomIdOrNegative(savedUser1.getId(), savedUser2.getId());

        // then
        assert roomId.equals(savedChatRoom1.getId());

    }
}