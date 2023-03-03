package com.sparta.sbug.chat.controller;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.service.ChatService;
import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Slice<ChatResponseDto> readMessagesInChatRoom(@PathVariable Long roomId, @ModelAttribute PageDto pageDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.readAllMessageInChatRoom(userDetails.getUser().getId(), roomId, pageDto);
    }

    /**
     * 요청자가 속한 채팅방에서 아직 읽지 않은 메세지의 개수들을 조회합니다.
     * [GET] /api/chats/rooms
     *
     * @param userDetails 요청자
     * @return List&lt;NewMessageCountDto&gt;
     */
    @GetMapping("/rooms")
    public Map<Long, Long> countNewMessageByChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.countNewMessageByChatRoom(userDetails.getUser());
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
