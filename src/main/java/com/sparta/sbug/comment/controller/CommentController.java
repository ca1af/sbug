package com.sparta.sbug.comment.controller;

import com.sparta.sbug.aop.ExeTimer;
import com.sparta.sbug.comment.dto.CommentRequestDto;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.userchannel.service.UserChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.sbug.common.exceptions.ErrorCode.USER_COMMENT_FORBIDDEN;
import static com.sparta.sbug.common.exceptions.ErrorCode.USER_THREAD_FORBIDDEN;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final UserChannelService userChannelService;
    private final ThreadService threadService;

    // CRUD

    /**
     * 쓰레드에 댓글을 생성
     * [POST] /api/channels/{channelId}/threads/{threadId}/comments
     *
     * @param channelId   채널 ID
     * @param threadId    쓰레드 ID
     * @param requestDto  댓글 요청 DTO (내용)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/channels/{channelId}/threads/{threadId}/comments")
    public CommentResponseDto createComment(@PathVariable Long channelId,
                                            @PathVariable Long threadId,
                                            @RequestBody @Valid CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }

        return threadService.createComment(threadId, requestDto.getContent(), userDetails.getUser());
    }

    /**
     * 쓰레드에 달린 모든 댓글을 조회
     * [GET] /api/channels/{channelId}/threads/{threadId}/comments
     *
     * @param channelId 채널 ID
     * @param threadId  쓰레드 ID
     * @param pageDto   페이징 DTO
     * @return Slice&lt;CommentResponseDto&gt;
     */
    @GetMapping("/channels/{channelId}/threads/{threadId}/comments")
    @ExeTimer
    public Slice<CommentResponseDto> getAllCommentsInThread(
            @PathVariable Long channelId,
            @PathVariable Long threadId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute PageDto pageDto) {

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }

        return commentService.getAllCommentsInThread(threadId, pageDto);
    }

    /**
     * 대상 댓글을 수정
     * [PATCH] /api/comments/{id}
     *
     * @param commentId   댓글 ID
     * @param requestDto  댓글 요청 DTO (내용)
     * @param userDetails 요청자 정보
     */
    @PatchMapping("/comments/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestBody CommentRequestDto requestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(commentId, requestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 댓글을 삭제
     * [DELETE] /api/comments/{id}
     *
     * @param commentId   댓글 ID
     * @param userDetails 요청자 정보
     */
    @PutMapping("/comments/{commentId}")
    public void disableComment(@PathVariable Long commentId,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.disableComment(commentId, userDetails.getUser());
    }

    // 이모지 반응
    @PostMapping("/channels/{channelId}/threads/comments/{commentId}/emojis")
    public boolean reactThreadEmoji(
            @PathVariable Long channelId,
            @PathVariable Long commentId,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_COMMENT_FORBIDDEN);
        }
        return commentService.reactCommentEmoji(emojiType, userDetails.getUser(), commentId);
    }

}
