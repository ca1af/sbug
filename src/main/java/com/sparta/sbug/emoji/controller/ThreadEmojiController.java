package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.dto.EmojiRequestDto;
import com.sparta.sbug.emoji.service.ThreadEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ThreadEmojiController {
    private final ThreadEmojiServiceImpl threadEmojiService;

    // api/channel/threads/{id}/emoji?type={emojiType}

    // ThreadEmoji 생성
    @PostMapping("/threads{id}")
    public String createThreadEmoji(
            @PathVariable Long id,
            @RequestBody EmojiRequestDto emojiRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.createThreadEmoji(id, emojiRequestDto, userDetails.getUser());
    }


    // ThreadEmoji 삭제
    @DeleteMapping("/threads{id}")
    public String deleteThreadEmoji(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadEmojiService.deleteThreadEmoji(id, userDetails.getUser());
    }
}
