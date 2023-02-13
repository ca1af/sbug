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


///api/channels/threads/comments/{id}/emojis?emoji-type={emojiType}
///api/channels/threads/comments/emojis/{id}

    // CommentEmoji 생성
    @PostMapping("/{id}/emojis")
    public String commentEmoji(
            @PathVariable Long id,
            @RequestParam(name = "emoji-type") String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.createCommentEmoji(id, emojiType,userDetails.getUser());
    }


    // CommentEmoji 삭제
    @DeleteMapping("/emojis/{id}")
    public String commentEmoji(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.deleteCommentEmoji(id, userDetails.getUser());
    }
}
