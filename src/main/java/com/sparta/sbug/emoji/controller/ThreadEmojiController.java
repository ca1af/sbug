package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.service.ThreadEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels/threads")
public class ThreadEmojiController {
    private final ThreadEmojiServiceImpl threadEmojiService;


    // ThreadEmoji 생성
    @PostMapping("/{id}/emojis")
    public String reactToThread(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.createThreadEmoji(emojiType, userDetails.getUser(), id);
    }


    // ThreadEmoji 삭제
    @DeleteMapping("/{id}/emojis")
    public String cancelReactToThread(
        @PathVariable Long id,
        @RequestBody String emojiType,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.deleteThreadEmoji(emojiType, userDetails.getUser(), id);
    }
}
