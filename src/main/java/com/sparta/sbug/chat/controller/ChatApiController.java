package com.sparta.sbug.chat.controller;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.service.ChatService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework web bind
@RestController
@RequestMapping("/api/chats")
public class ChatApiController {

    private final ChatService chatService;

    /**
     * 채팅 방의 이전 메세지들을 불러오는 함수입니다.
     * (페이징되며, 프론트에서 "더보기"를 선택해 다음 페이지를 불러올 것입니다)
     * [GET] /api/chats/rooms/{roomId}/messages
     *
     * @param roomId      채팅방 ID
     * @param pageDto     페이징 DTO
     * @param userDetails 요청자 정보
     * @return List&lt;ChatResponseDto&gt;
     */
    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatResponseDto> readMessagesInChatRoom(@PathVariable Long roomId, @ModelAttribute PageDto pageDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.readAllMessageInChatRoom(userDetails.getUser().getId(), roomId, pageDto);
    }

    /**
     * 메세지 아이디로 메세지를 찾고 해당 메세지의 내용을 업데이트 합니다.
     * [PATCH] /api/chats/messages/{id}
     *
     * @param id          메세지의 아이디입니다.
     * @param message     이 메세지 내용으로 업데이트 합니다.
     * @param userDetails 요청한 유저의 정보입니다. 이 정보는 메세지를 업데이트 할 권한이 있는지 검증하는데 사용됩니다.
     */
    @PatchMapping("/messages/{id}")
    public void updateMessage(@PathVariable Long id, @RequestParam String message,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.updateMessage(id, userDetails.getUser(), message);
    }

    /**
     * 메세지 아이디로 메세지를 찾고 해당 메세지의 내용을 삭제 합니다.
     * [DELETE] /api/chats/messages/{id}
     *
     * @param id          메세지의 아이디입니다.
     * @param userDetails 메세지를 삭제 할 권한이 있는지 검증하는데 사용될 요청한 유저의 정보입니다.
     */
    @DeleteMapping("/messages/{id}")
    public void deleteMessage(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.deleteMessage(id, userDetails.getUser());
    }

    /**
     * 요청자가 아직 읽지 않은 메세지들의 개수를 찾습니다.
     * [GET] /api/chats/new-messages
     *
     * @param userDetails 요청자에게 온 메세지만을 찾기 위해 이 유저 정보가 사용됩니다.
     * @return Long : 읽지 않은 메세지들의 개수입니다.
     */
    @GetMapping("/new-messages")
    public Long countNewMessages(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.countNewMessages(userDetails.getUser());
    }

}
