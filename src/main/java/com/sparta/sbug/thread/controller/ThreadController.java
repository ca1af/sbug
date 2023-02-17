package com.sparta.sbug.thread.controller;

import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.service.ThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api/channels")
public class ThreadController {

    private final ThreadService threadService;
    private final ChannelService channelService;

    /**
     * 대상 채널에 쓰레드를 생성
     * [POST] /api/channels/{id}/threads
     *
     * @param id               대상 채널 ID
     * @param threadRequestDto 요청 DTO (쓰레드 내용)
     * @param userDetails      요청자 정보
     */
    @PostMapping("/{id}/threads")
    public ThreadResponseDto createThread(
            @PathVariable Long id, @RequestBody @Valid ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (threadRequestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("작성할 쓰레드 내용을 입력해주세요.");
        }
        return channelService.createThread(id, threadRequestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 쓰레드를 수정
     * [PATCH] /api/channels/threads/{id}
     *
     * @param id               대상 쓰레드 ID
     * @param threadRequestDto 요청 DTO (수정될 쓰레드 내용)
     * @param userDetails      요청자 정보
     */
    @PatchMapping("/threads/{id}")
    public void updateThread(
            @PathVariable Long id, @RequestBody ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (threadRequestDto.getContent().trim().equals("")) {
            throw new IllegalArgumentException("수정할 쓰레드 내용을 입력해주세요.");
        }
        threadService.editThread(id, threadRequestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 쓰레드를 삭제
     * [DELETE] /api/channels/threads/{id}
     *
     * @param id          대상 쓰레드 ID
     * @param userDetails 요청자 정보
     */
    @DeleteMapping("/threads/{id}")
    public void deleteThread(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        threadService.disableThread(id, userDetails.getUser());
    }

    /**
     * 대상 채널 아래 모든 쓰레드를 조회
     * [GET] /api/channels/{id}/threads
     *
     * @param id      대상 채널
     * @param pageDto 페이징 DTO
     * @return List&lt;ThreadResponseDto&gt;
     */
    @GetMapping("/{id}/threads")
    public List<ThreadResponseDto> getAllThreadsInChannel(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @ModelAttribute PageDto pageDto) {
        log.info("[GET] /" + id + "/threads");
        channelService.validateUserInChannel(id, userDetails.getUser());
        return threadService.getAllThreadsInChannel(id, pageDto);
    }

    /**
     * 대상 쓰레드를 조회
     * [GET] /api/channels/threads/{id}
     *
     * @param channelId 대상 채널
     * @param threadId  대상 쓰레드
     * @return ThreadResponseDto
     */
    @GetMapping("{channelId}/threads/{threadId}")
    public ThreadResponseDto getThread(@PathVariable Long channelId,
                                       @PathVariable Long threadId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("[GET] /" + channelId + "/threads/" + threadId);
        channelService.validateUserInChannel(channelId, userDetails.getUser());
        return threadService.getThread(threadId);
    }
}
