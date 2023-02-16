package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.service.CommentEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/channels/threads/comments")
public class CommentEmojiController {

    private final CommentEmojiServiceImpl commentEmojiService;

    /**
     * 댓글 이모지 반응을 생성
     * [POST] /api/channels/threads/comments/{id}/emojis
     *
     * @param id          댓글 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/{id}/emojis")
    public void createCommentEmoji(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentEmojiService.createCommentEmoji(emojiType, userDetails.getUser(), id);
    }


    /**
     * 댓글 이모지 반응을 삭제
     * [DELETE] /api/channels/threads/comments/{id}/emojis
     *
     * @param id          댓글 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @DeleteMapping("/{id}/emojis")
    public void deleteCommentEmoji(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentEmojiService.deleteCommentEmoji(emojiType, userDetails.getUser(), id);
    }
}
