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

// springframework web bind
@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;
    private final ThreadCommentUpperLayerService threadCommentUpperLayerService;

    /**
     * 쓰레드에 달린 모든 댓글을 조회
     * [GET] /api/threads/{id}/comments
     *
     * @param id      쓰레드 ID
     * @param pageDto 페이징 DTO
     * @return List&lt;CommentResponseDto&gt;
     */
    @GetMapping("/threads/{id}/comments")
    public List<CommentResponseDto> getAllCommentsInThread(@PathVariable Long id, @ModelAttribute PageDto pageDto) {
        return commentService.getAllCommentsInThread(id, pageDto);
    }

    /**
     * 쓰레드에 댓글을 생성
     * [POST] /api/threads/{id}/comments
     *
     * @param id          쓰레드 ID
     * @param requestDto  댓글 요청 DTO (내용)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/threads/{id}/comments")
    public void createComment(@PathVariable Long id, @RequestBody @Valid CommentRequestDto requestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 내용이 공백인지 확인 -> 서비스로 옮기기
        if (requestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("작성할 댓글 내용을 입력해주세요");
        }

        // 댓글 생성
        threadCommentUpperLayerService.createComment(id, requestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 댓글을 수정
     * [PATCH] /api/comments/{id}
     *
     * @param id          댓글 ID
     * @param requestDto  댓글 요청 DTO (내용)
     * @param userDetails 요청자 정보
     */
    @PatchMapping("/comments/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 내용이 공백인지 확인 -> 서비스로 옮기기
        if (requestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("수정할 댓글 내용을 입력해주세요");
        }

        // 댓글 내용 수정
        threadCommentUpperLayerService.updateComment(id, requestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 댓글을 삭제
     * [DELETE] /api/comments/{id}
     *
     * @param id          댓글 ID
     * @param userDetails 요청자 정보
     */
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        threadCommentUpperLayerService.deleteComment(id, userDetails.getUser());
    }

}
