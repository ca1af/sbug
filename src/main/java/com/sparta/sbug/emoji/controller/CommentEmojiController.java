package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.dto.EmojiRequestDto;
import com.sparta.sbug.emoji.service.CommentEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class CommentEmojiController {
    private final CommentEmojiServiceImpl commentEmojiService;

 //   api/channel/threads/comments/{id}/emoji?type={emojiType}

    // CommentEmoji 생성
    @PostMapping("/threads/comments/{id}")
    public String createCommentEmoji(
            @PathVariable Long id,
            @RequestBody EmojiRequestDto emojiRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.createCommentEmoji(id, emojiRequestDto, userDetails.getUser());
    }


    // CommentEmoji 삭제
    @DeleteMapping("/threads/comments/{id}")
    public String deleteCommentEmoji(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.deleteCommentEmoji(id, userDetails.getUser());
    }
}
