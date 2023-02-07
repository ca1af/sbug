package com.sparta.sbug.chat.service;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ChatServiceImpl implements ChatService {



    @Override
    public List<ChatResponseDto> getAllExchangedMessage(Long myUserId, Long theOtherUserId) {
        return null;
    }

    @Override
    public void sendMessage(Long receiverId, String message) {

    }

    @Override
    public void updateMessage(Long messageId, String message) {

    }

    @Override
    public void deleteMessage(Long messageId) {

    }
}
