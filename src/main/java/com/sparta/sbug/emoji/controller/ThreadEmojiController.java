package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.service.ThreadEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel/threads")
public class ThreadEmojiController {
    private final ThreadEmojiServiceImpl threadEmojiService;

    // api/channel/threads/{id}/emoji?emojiType={emojiType}

    // ThreadEmoji 생성
    @PostMapping("/{threadId}/emoji")
    public String createThreadEmoji(
            @PathVariable Long threadId,
            @RequestParam(name = "emoji-type") String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.createThreadEmoji(threadId, emojiType, userDetails.getUser());
    }


    // ThreadEmoji 삭제
    @DeleteMapping("/{emojiId}/emoji")
    public String deleteThreadEmoji(
        @PathVariable Long emojiId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.deleteThreadEmoji(emojiId, userDetails.getUser());
    }
}
