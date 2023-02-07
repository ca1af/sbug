package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import com.sparta.sbug.chat.repository.ChatRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ChatServiceImpl implements ChatService {


    private final ChatRepository chatRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponseDto> readAllExchangedMessage(Long receiver, Long sender, PageDto pageDto) {
        Page<Chat> pageChats = chatRepository.findExchangedMessages(receiver, sender, pageDto.toPageable());
        for (Chat chat : pageChats.getContent()) {
            if (chat.getReceiver().getId().equals(receiver)) {
                chat.markToRead();
            }
        }
        return getDtoListFromEntities(pageChats);
    }

    @Override
    @Transactional
    public String sendMessage(User sender, User receiver, String message) {
        Chat chat = Chat.builder()
                .sender(sender)
                .message(message)
                .receiver(receiver).build();
        chatRepository.save(chat);
        return "Success";
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
    public Long countNewMessages(User user, PageDto pageDto) {
        return chatRepository.countByReceiverIdAndStatus(user.getId(), ChatStatus.NEW, pageDto.toPageable());
    }

    @Transactional(readOnly = true)
    public Chat findChatById(Long messageId) {
        Optional<Chat> optionalChat = chatRepository.findById(messageId);
        if (optionalChat.isEmpty()) {
            // 수정 예정, 리소스를 찾을 수 없을 때
            throw new IllegalArgumentException();
        }

        return optionalChat.get();
    }

    @Transactional(readOnly = true)
    public Chat validateUserIsSender(Chat chat, User user) {
        if (!chat.getSender().equals(user)) {
            // 수정 예정, 권한이 없을 때
            throw new IllegalArgumentException();
        }

        return chat;
    }

    public List<ChatResponseDto> getDtoListFromEntities(Page<Chat> pageChats) {
        List<Chat> chats = pageChats.getContent();
        List<ChatResponseDto> chatResponseDtoList = new ArrayList<>();
        for (Chat chat : chats) {
            ChatResponseDto chatResponseDto =  ChatResponseDto.builder()
                    .id(chat.getId())
                    .sender(chat.getSender().getNickname())
                    .message(chat.getMessage())
                    .receiver(chat.getReceiver().getNickname())
                    .status(chat.getStatus().toString()).build();
            chatResponseDtoList.add(chatResponseDto);
        }
        return chatResponseDtoList;
    }
}
