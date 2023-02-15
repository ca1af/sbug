package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.service.CommentEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels/threads/comments")
public class CommentEmojiController {
    private final CommentEmojiServiceImpl commentEmojiService;


    // CommentEmoji 생성
    @PostMapping("/{id}/emojis")
    public String ReactToComment(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.createCommentEmoji(emojiType, userDetails.getUser(), id);
    }


    // CommentEmoji 삭제
    @DeleteMapping("/{id}/emojis")
    public String cancelReactToComment(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.deleteCommentEmoji(emojiType, userDetails.getUser(), id);
    }
}
