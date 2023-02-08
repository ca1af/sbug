package com.sparta.sbug.chat.controller;

import com.sparta.sbug.chat.dto.ChatRequestDto;
import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.service.ChatService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @GetMapping("/users/{theOtherUserId}/messages")
    public List<ChatResponseDto> readAllExchangedMessages(@PathVariable Long theOtherUserId, PageDto pageDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.readAllExchangedMessage(userDetails.getUser().getId(), theOtherUserId, pageDto);
    }

    @PostMapping("/users/{receiverId}/message")
    public String sendMessage(@PathVariable Long receiverId, @RequestBody ChatRequestDto requestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User receiver = userService.getUserById(receiverId);
        return chatService.sendMessage(userDetails.getUser(), receiver, requestDto.getMessage());
    }

    @PatchMapping("/messages/{id}")
    public String updateMessage(@PathVariable Long id, @RequestBody ChatRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.updateMessage(id, userDetails.getUser(), requestDto.getMessage());
    }

    @DeleteMapping("/messages/{id}")
    public String deleteMessage(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.deleteMessage(id, userDetails.getUser());
    }

    @GetMapping("/count-new-messages")
    public Long countNewMessages(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.countNewMessages(userDetails.getUser());
    }
}
