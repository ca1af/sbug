package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import com.sparta.sbug.chat.repository.ChatRepository;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.websocket.handler.ChatPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ChatServiceImpl implements ChatService {
    
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public Slice<ChatResponseDto> readAllMessageInChatRoom(Long requesterId, Long roomId, PageDto pageDto) {
        Slice<ChatResponseDto> sliceChats = chatRepository.findMessagesInChatRoom(roomId, pageDto.toPageable());
        List<Long> chatIds = sliceChats.stream()
                .filter(chatResponseDto -> chatResponseDto.getReceiverId().equals(requesterId))
                .map(ChatResponseDto::getId).toList();
        chatRepository.convertMessagesStatusToRead(chatIds);
        return sliceChats;
    }

    @Override
    @Transactional
    public ChatResponseDto createMessage(ChatRoom chatRoom, User sender, User receiver, String message) {
        Chat chat = Chat.builder()
                .room(chatRoom)
                .sender(sender)
                .message(message)
                .receiver(receiver).build();
        if (ChatPreHandler.CHAT_ROOM_USER_MAP.containsKey("/topic/chats/rooms/" + chatRoom.getId())) {
            Set<String> tests = ChatPreHandler.CHAT_ROOM_USER_MAP.get("/topic/chats/rooms/" + chatRoom.getId());
            if (tests.size() > 1) {
                chat.setStatus(ChatStatus.READ);
            }
        }
        Chat savedChat = chatRepository.save(chat);
        return ChatResponseDto.of(savedChat);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Long> countNewMessageByChatRoom(User user) {
        return chatRepository.countNewMessageByChatRoom(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countNewMessages(User user) {
        return chatRepository.countNewMessageByReceiverId(user.getId());
    }
}
