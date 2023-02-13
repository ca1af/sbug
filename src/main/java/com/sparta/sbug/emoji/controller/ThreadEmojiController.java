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

///api/channels/threads/{id}/emojis?emoji-type={emojiType}
///api/channels/threads/emojis/{id}

    // ThreadEmoji 생성
    @PostMapping("/emojis/{id}")
    public String threadEmoji(
            @PathVariable Long id,
            @RequestParam(name = "emoji-type") String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.createThreadEmoji(id, emojiType, userDetails.getUser());
    }


    // ThreadEmoji 삭제
    @DeleteMapping("/emojis/{id}")
    public String threadEmoji(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.deleteThreadEmoji(id, userDetails.getUser());
    }
}
