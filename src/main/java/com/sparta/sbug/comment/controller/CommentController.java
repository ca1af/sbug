package com.sparta.sbug.comment.controller;

import com.sparta.sbug.comment.dto.CommentRequestDto;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;
    private final ThreadService threadService;

    @GetMapping("/threads/{id}/comments")
    public List<CommentResponseDto> getAllCommentsInThread(@PathVariable Long id, PageDto pageDto) {
        return commentService.getAllCommentsInThread(id, pageDto);
    }

    @PostMapping("/threads/{id}/comments")
    public String createMessage(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Thread thread = threadService.findThreadById(id);
        return commentService.createComment(thread, requestDto.getContent(), userDetails.getUser());
    }

    @PatchMapping("/comments/{id}")
    public String updateMessage(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto.getContent(), userDetails.getUser().getId());
    }

    @DeleteMapping("/comments/{id}")
    public String deleteMessage(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser().getId());
    }

}
