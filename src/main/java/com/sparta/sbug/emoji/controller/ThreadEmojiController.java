package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.service.ThreadEmojiService;
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
public class ThreadEmojiController {

    private final ThreadEmojiService threadEmojiService;
    private final UserChannelService userChannelService;

    /**
     * 쓰레드 이모지 반응을 생성하거나 이미 동일한 반응이 존재한다면 삭제
     * [POST] /api/channels/{channelId}/threads/{threadId}/emojis
     *
     * @param channelId   채널 ID
     * @param threadId    쓰레드 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/{channelId}/threads/{threadId}/emojis")
    public boolean reactThreadEmoji(
            @PathVariable Long channelId,
            @PathVariable Long threadId,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }
        return threadEmojiService.reactThreadEmoji(emojiType, userDetails.getUser(), threadId);
    }

    /**
     * 쓰레드 이모지 반응을 삭제
     * [DELETE] /api/channels/{channelId}/threads/{threadId}/emojis
     *
     * @param threadId    쓰레드 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @DeleteMapping("/threads/{threadId}/emojis")
    public void cancelReactToThread(
            @PathVariable Long threadId,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        threadEmojiService.deleteThreadEmoji(emojiType, userDetails.getUser(), threadId);
    }
}
