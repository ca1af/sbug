package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import com.sparta.sbug.chat.repository.ChatRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    @DisplayName("Chat: Send Message")
    void sendMessage() {
        // given
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        // when
        String result1 = chatService.sendMessage(savedUser1, savedUser2, "message1");
        String result2 = chatService.sendMessage(savedUser1, savedUser2, "message2");
        System.out.println("======================= send query start! =======================");
        String result3 = chatService.sendMessage(savedUser2, savedUser1, "message3");

        // then
        assert result1.equals("Success");
        assert result2.equals("Success");
        assert result3.equals("Success");
    }

    @Test
    @DisplayName("Chat: Find Exchanged Messages")
    void readExchangedMessages() {
        // given
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        chatService.sendMessage(savedUser1, savedUser2, "message1");
        chatService.sendMessage(savedUser1, savedUser2, "message2");
        chatService.sendMessage(savedUser2, savedUser1, "message3");

        PageDto pageDto = PageDto.builder().currentPage(1).size(5).sortBy("createdAt").build();

        // when
        System.out.println("======================= test query start! =======================");
        var chats = chatService.readAllExchangedMessage(savedUser1.getId(), savedUser2.getId(), pageDto);

        // then
        assert chats.stream().map(ChatResponseDto::getMessage).toList().equals(List.of("message1", "message2", "message3"));
        assert chats.stream().map(ChatResponseDto::getStatus).toList().equals(List.of("NEW", "NEW", "READ"));
    }

    @Test
    @DisplayName("Chat: Update Messages")
    void updateMessage() {
        // give
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        Chat chat = Chat.builder().sender(savedUser1).message("before message").receiver(savedUser2).build();
        Chat savedChat = chatRepository.save(chat);

        // when
        System.out.println("======================= test query start! =======================");
        String result = chatService.updateMessage(savedChat.getId(), savedUser1, "after message");

        // then
        assert result.equals("Success");
        assert chatRepository.findById(savedChat.getId()).get().getMessage().equals("after message");
    }

    @Test
    @DisplayName("Chat: Delete Messages")
    void deleteMessage() {
        // give
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        Chat chat = Chat.builder().sender(savedUser1).message("before message").receiver(savedUser2).build();
        Chat savedChat = chatRepository.save(chat);

        // when
        System.out.println("======================= test query start! =======================");
        String result = chatService.deleteMessage(savedChat.getId(), savedUser1);

        // then
        assert result.equals("Success");
        assert chatRepository.findById(savedChat.getId()).isEmpty();
    }

    @Test
    @DisplayName("Chat: Count New Messages")
    void countNewMessages() {
        // give
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        Chat chat1 = Chat.builder().sender(savedUser1).message("message1").receiver(savedUser2).build();
        Chat chat2 = Chat.builder().sender(savedUser1).message("message2").receiver(savedUser2).build();
        Chat chat3 = Chat.builder().sender(savedUser1).message("message3").receiver(savedUser2).build();
        chat3.markToRead();
        Chat savedChat1 = chatRepository.save(chat1);
        Chat savedChat2 = chatRepository.save(chat2);
        Chat savedChat3 = chatRepository.save(chat3);

        PageDto pageDto = PageDto.builder().currentPage(1).size(5).sortBy("createdAt").build();

        // when
        System.out.println("======================= test query start! =======================");
        var count = chatService.countNewMessages(savedUser2, pageDto);

        // then
        assert count == 2;
    }

}