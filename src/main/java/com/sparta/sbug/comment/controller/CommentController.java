package com.sparta.sbug.comment.controller;

import com.sparta.sbug.comment.dto.CommentRequestDto;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.upperlayerservice.ThreadCommentUpperLayerService;
import jakarta.validation.Valid;
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
    private final ThreadCommentUpperLayerService threadCommentUpperLayerService;

    @GetMapping("/threads/{id}/comments")
    public List<CommentResponseDto> getAllCommentsInThread(@PathVariable Long id, @ModelAttribute PageDto pageDto) {
        return commentService.getAllCommentsInThread(id, pageDto);
    }

    @PostMapping("/threads/{id}/comments")
    public String createMessage(@PathVariable Long id, @RequestBody @Valid CommentRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (requestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("작성할 댓글 내용을 입력해주세요");
        }
        return threadCommentUpperLayerService.createComment(id, requestDto.getContent(), userDetails.getUser());
    }

    @PatchMapping("/comments/{id}")
    public String updateMessage(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (requestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("수정할 댓글 내용을 입력해주세요");
        }
        return threadCommentUpperLayerService.updateComment(id, requestDto.getContent(), userDetails.getUser());
    }

    @DeleteMapping("/comments/{id}")
    public String deleteMessage(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return threadCommentUpperLayerService.deleteComment(id, userDetails.getUser());
    }

}
