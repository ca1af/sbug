package com.sparta.sbug.comment.controller;

import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.dto.CommentRequestDto;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.service.ThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api/channels/")
public class CommentController {

    private final CommentService commentService;
    private final ChannelService channelService;
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
    @PostMapping("{channelId}/threads/{threadId}/comments")
    public CommentResponseDto createComment(@PathVariable Long channelId,
                                            @PathVariable Long threadId,
                                            @RequestBody @Valid CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[POST] /api/channels/" + channelId + "/threads/" + threadId + "/comments";
        log.info(logBuilder);

        channelService.validateUserInChannel(channelId, userDetails.getUser());

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
    @GetMapping("{channelId}/threads/{threadId}/comments")
    public Slice<CommentResponseDto> getAllCommentsInThread(
            @PathVariable Long channelId,
            @PathVariable Long threadId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute PageDto pageDto) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads/" + threadId + "/comments";

        log.info(logBuilder);
        channelService.validateUserInChannel(channelId, userDetails.getUser());
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

        String logBuilder = "[PATCH] /api/comments/" + commentId;
        log.info(logBuilder);

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

        String logBuilder = "[PUT] /api/comments/" + commentId;
        log.info(logBuilder);

        commentService.disableComment(commentId, userDetails.getUser());
    }

}
