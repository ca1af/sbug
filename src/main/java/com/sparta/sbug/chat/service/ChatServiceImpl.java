package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import com.sparta.sbug.chat.repository.ChatRepository;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ChatServiceImpl implements ChatService {


    private final ChatRepository chatRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponseDto> readAllMessageInChatRoom(Long requesterId, Long roomId, PageDto pageDto) {
        Page<Chat> pageChats = chatRepository.findMessagesInChatRoom(roomId, pageDto.toPageable());
        convertToRead(requesterId, pageChats);
        return getDtoListFromEntities(pageChats);
    }

    @Override
    @Transactional
    public void convertToRead(Long requesterId, Page<Chat> chats) {
        for (Chat chat : chats.getContent()) {
            if (chat.getReceiver().getId().equals(requesterId)) {
                chat.setStatus(ChatStatus.READ);
            }
        }
    }

    @Override
    public List<ChatResponseDto> getDtoListFromEntities(Page<Chat> pageChats) {
        List<Chat> chats = pageChats.getContent();
        return chats.stream().map(ChatResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatResponseDto createMessage(ChatRoom chatRoom, User sender, User receiver, String message) {
        Chat chat = Chat.builder()
                .room(chatRoom)
                .sender(sender)
                .message(message)
                .receiver(receiver).build();
        Chat savedChat = chatRepository.saveAndFlush(chat);
        return ChatResponseDto.of(savedChat);
    }

    @Override
    @Transactional
    public String updateMessage(Long messageId, User user, String message) {
        Chat chat = findChatById(messageId);
        validateUserIsSender(chat, user);
        chat.updateMessage(message);
        chatRepository.save(chat);
        return "Success";
    }

    @Override
    @Transactional
    public String deleteMessage(Long messageId, User user) {
        Chat chat = findChatById(messageId);
        validateUserIsSender(chat, user);
        chatRepository.delete(chat);
        return "Success";
    }

    @Override
    @Transactional(readOnly = true)
    public Chat findChatById(Long messageId) {
        return chatRepository.findById(messageId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUserIsSender(Chat chat, User user) {
        if (!chat.getSender().equals(user)) {
            // 수정 예정, 권한이 없을 때
            throw new IllegalArgumentException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long countNewMessages(User user) {
        return chatRepository.countByReceiverIdAndStatus(user.getId(), ChatStatus.NEW);
    }
}
