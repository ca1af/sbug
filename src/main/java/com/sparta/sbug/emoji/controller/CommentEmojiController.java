package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.service.CommentEmojiService;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sbug.common.exceptions.ErrorCode.USER_THREAD_FORBIDDEN;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/channels")
public class CommentEmojiController {

    private final CommentEmojiService commentEmojiService;
    private final UserChannelService userChannelService;

    /**
     * 댓글 이모지 반응을 생성하거나 이미 동일한 반응이 존재한다면 삭제
     * [POST] /api/channels/{channelId}/threads/comments/{commentId}/emojis
     *
     * @param channelId   채널 ID
     * @param commentId   댓글 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/{channelId}/threads/comments/{commentId}/emojis")
    public boolean reactThreadEmoji(
            @PathVariable Long channelId,
            @PathVariable Long commentId,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }
        return commentEmojiService.reactCommentEmoji(emojiType, userDetails.getUser(), commentId);
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
