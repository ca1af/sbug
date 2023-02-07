package com.sparta.sbug.thread.controller;

import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.service.ThreadServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class ThreadController {
    private final ThreadServiceImpl threadService;

    // Thread 작성
    @PostMapping("/api/channels/{id}/thread")
    public String createThread(
            @RequestParam Long channelId,
            @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return threadService.createThread(channelId, threadRequestDto, userDetails.getUser());
    }


    // Thread 수정
    @PatchMapping("/api/channel/threads{threadId}")
    public String editThread(
            @PathVariable Long threadId,
            @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return threadService.editThread(threadId, threadRequestDto,userDetails.getUser());
    }


    // Tread 삭제
    @DeleteMapping("/api/channel/threads{threadId}")
    public String deleteThread(
            @PathVariable Long threadId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return  threadService.deleteThread(threadId, userDetails.getUser());
    }

}
