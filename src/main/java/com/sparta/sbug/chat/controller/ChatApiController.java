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

// springframework
@RestController
@RequestMapping("/api/chats")
public class ChatApiController {

    private final ChatService chatService;

    /**
     * PageDto에 따라 메세지를 불러오는 함수입니다.
     * 프론트에서 "더보기"를 선택해 다음 페이지를 불러올 때 호출될 것이라고 생각됩니다.
     *
     * @param currentPage : 현재 페이지를 나타내는 숫자입니다. 이 페이지 숫자에서 +1 하여 다음 페이지를 불러오도록 합니다.
     */
    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatResponseDto> readMessagesInChatRoom(@PathVariable Long roomId, @RequestParam int currentPage,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PageDto pageDto = PageDto.builder().currentPage(currentPage + 1).size(5).sortBy("createdAt").build();
        return chatService.readAllMessageInChatRoom(userDetails.getUser().getId(), roomId, pageDto);
    }

    /**
     * 메세지 아이디로 메세지를 찾고 해당 메세지의 내용을 업데이트 합니다.
     *
     * @param id          : 메세지의 아이디입니다.
     * @param message     : 이 메세지 내용으로 업데이트 합니다.
     * @param userDetails : 요청한 유저의 정보입니다. 이 정보는 메세지를 업데이트 할 권한이 있는지 검증하는데 사용됩니다.
     */
    @PatchMapping("/messages/{id}")
    public String updateMessage(@PathVariable Long id, @RequestParam String message,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.updateMessage(id, userDetails.getUser(), message);
    }

    /**
     * 메세지 아이디로 메세지를 찾고 해당 메세지의 내용을 삭제 합니다.
     *
     * @param id          : 메세지의 아이디입니다.
     * @param userDetails : 메세지를 삭제 할 권한이 있는지 검증하는데 사용될 요청한 유저의 정보입니다.
     */
    @DeleteMapping("/messages/{id}")
    public String deleteMessage(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.deleteMessage(id, userDetails.getUser());
    }

    /**
     * 요청자가 아직 읽지 않은 메세지들의 개수를 찾습니다.
     *
     * @param userDetails : 요청자에게 온 메세지만을 찾기 위해 이 유저 정보가 사용됩니다.
     */
    @GetMapping("/new-messages")
    public Long countNewMessages(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.countNewMessages(userDetails.getUser());
    }

}
