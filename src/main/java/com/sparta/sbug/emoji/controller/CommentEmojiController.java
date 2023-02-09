package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.service.CommentEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel/threads/comments")
public class CommentEmojiController {
    private final CommentEmojiServiceImpl commentEmojiService;

 //  api/channel/threads{threadId}/comments/{commentId}/emoji?emojiType={emojiType}

    // CommentEmoji 생성
    @PostMapping("/{commentId}")
    public String commentEmoji(
            @PathVariable Long commentId,
            @RequestParam(name = "emoji-type") String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.createCommentEmoji(commentId, emojiType,userDetails.getUser());
    }


    // CommentEmoji 삭제
    @DeleteMapping("/{emojiId}")
    public String commentEmoji(
            @PathVariable Long emojiId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentEmojiService.deleteCommentEmoji(emojiId, userDetails.getUser());
    }
}
