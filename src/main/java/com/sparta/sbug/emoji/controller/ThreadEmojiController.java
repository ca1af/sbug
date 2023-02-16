package com.sparta.sbug.emoji.controller;

import com.sparta.sbug.emoji.service.ThreadEmojiServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/channels/threads")
public class ThreadEmojiController {

    private final ThreadEmojiServiceImpl threadEmojiService;

    /**
     * 쓰레드 이모지 반응을 생성
     * [POST] /api/channels/threads/{id}/emojis
     *
     * @param id          쓰레드 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/{id}/emojis")
    public void reactToThread(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        threadEmojiService.createThreadEmoji(emojiType, userDetails.getUser(), id);
    }


    /**
     * 쓰레드 이모지 반응을 삭제
     * [DELETE] /api/channels/threads/{id}/emojis
     *
     * @param id          쓰레드 iD
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @DeleteMapping("/{id}/emojis")
    public void cancelReactToThread(
            @PathVariable Long id,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        threadEmojiService.deleteThreadEmoji(emojiType, userDetails.getUser(), id);
    }
}
