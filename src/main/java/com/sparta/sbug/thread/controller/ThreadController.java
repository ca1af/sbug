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
    @PostMapping("/{channelId}/thread")
    public String thread(
            @PathVariable Long channelId,
            @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadService.createThread(channelId, threadRequestDto, userDetails.getUser());
    }

    // Thread 수정
    @PatchMapping("/threads/{threadId}")
    public String tread(
            @PathVariable Long threadId,
            @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return threadService.editThread(threadId, threadRequestDto,userDetails.getUser());
    }

    // Tread 삭제
    @DeleteMapping("/threads/{threadId}")
    public String thread(
            @PathVariable Long threadId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return threadService.deleteThread(threadId, userDetails.getUser().getId());
    }

    // 각 Thread에 대한 Comment 조회
    @GetMapping("/threads/{threadId}")
    public List<CommentResponseDto> comments(
            @PathVariable Long threadId){
        return threadService.getComments(threadId);
    }

}
