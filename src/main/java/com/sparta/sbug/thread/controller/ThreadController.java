package com.sparta.sbug.thread.controller;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.service.ThreadServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ThreadController {
    private final ThreadServiceImpl threadService;

   // Thread 작성
    @PostMapping("/{id}/threads")
    public String thread(
            @PathVariable Long id,
            @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadService.createThread(id, threadRequestDto, userDetails.getUser());
    }

    // Thread 수정
    @PatchMapping("/threads/{id}")
    public String tread(
            @PathVariable Long id,
            @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return threadService.editThread(id, threadRequestDto,userDetails.getUser());
    }

    // Tread 삭제
    @DeleteMapping("/threads/{id}")
    public String thread(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return threadService.deleteThread(id, userDetails.getUser().getId());
    }

    // Thread 아래 댓글 전체 조회(페이징)
    @GetMapping("/threads/{id}")
    public List<CommentResponseDto> comments(
            @PathVariable Long id){
        return threadService.getComments(id);
    }

}
