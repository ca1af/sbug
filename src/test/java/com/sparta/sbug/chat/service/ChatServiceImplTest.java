package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.repository.ChatRepository;
import com.sparta.sbug.chatroom.repository.ChatRoomRepository;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ChatServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ChatService chatService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("Chat: Send Message")
    void sendMessage() {

    }

    @Test
    @DisplayName("Chat: Find Exchanged Messages")
    void readExchangedMessages() {

    }

    @Test
    @DisplayName("Chat: Update Messages")
    void updateMessage() {

    }

    @Test
    @DisplayName("Chat: Delete Messages")
    void deleteMessage() {

    }

    @Test
    @DisplayName("Chat: Count New Messages")
    void countNewMessages() {

    }

}