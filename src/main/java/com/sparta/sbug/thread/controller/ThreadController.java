package com.sparta.sbug.thread.controller;

import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.service.ThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ThreadController {
    private final ThreadService threadService;

    private final ChannelService channelService;

   // Thread 작성
    @PostMapping("/{id}/threads")
    public String createThread(
            @PathVariable Long id,@RequestBody @Valid ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (threadRequestDto.getContent().trim().equals("")){
            throw new IllegalArgumentException("작성할 쓰레드 내용을 입력해주세요.");
        }
        return channelService.createThread(id, threadRequestDto.getContent(), userDetails.getUser());
    }

    // Thread 수정
    @PatchMapping("/threads/{id}")
    public String updateThread(
            @PathVariable Long id, @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (threadRequestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("수정할 쓰레드 내용을 입력해주세요.");
        }
        return channelService.editThread(id, threadRequestDto.getContent(), userDetails.getUser());
    }

    // Tread 삭제
    @DeleteMapping("/threads/{id}")
    public String deleteThread(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return channelService.deleteThread(id, userDetails.getUser());
    }

    @GetMapping("/{id}/threads")
    public List<ThreadResponseDto> getAllThreadsInChannel(
            @PathVariable Long id, @ModelAttribute PageDto pageDto){
        return threadService.getAllThreadsInChannel(id, pageDto);
    }

}
