package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.service.CommentEmojiService;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sbug.common.exceptions.ErrorCode.USER_COMMENT_FORBIDDEN;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/channels")
public class CommentEmojiController {

    private final CommentEmojiService commentEmojiService;
    private final UserChannelService userChannelService;

    @PostMapping("/{channelId}/threads/comments/{commentId}/emojis")
    public boolean reactThreadEmoji(
            @PathVariable Long channelId,
            @PathVariable Long commentId,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_COMMENT_FORBIDDEN);
        }
        return commentEmojiService.reactCommentEmoji(emojiType, userDetails.getUser(), commentId);
    }
}
