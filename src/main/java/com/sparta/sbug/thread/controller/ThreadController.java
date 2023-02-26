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
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api/channels")
public class ThreadController {

    private final ChannelService channelService;
    private final ThreadService threadService;

    /**
     * 대상 채널에 쓰레드를 생성
     * [POST] /api/channels/{id}/threads
     *
     * @param channelId        대상 채널 ID
     * @param threadRequestDto 요청 DTO (쓰레드 내용)
     * @param userDetails      요청자 정보
     */
    @PostMapping("/{channelId}/threads")
    public ThreadResponseDto createThread(
            @PathVariable Long channelId, @RequestBody @Valid ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[POST] api/channels/" + channelId + "/threads";
        log.info(logBuilder);

        channelService.validateUserInChannel(channelId, userDetails.getUser());

        return channelService.createThread(channelId, threadRequestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 채널 아래 모든 쓰레드를 조회
     * [GET] /api/channels/{channelId}/threads
     *
     * @param channelId 대상 채널
     * @param pageDto   페이징 DTO
     * @return List&lt;ThreadResponseDto&gt;
     */
    @GetMapping("/{channelId}/threads")
    public Slice<ThreadResponseDto> getAllThreadsInChannel(@PathVariable Long channelId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @ModelAttribute PageDto pageDto) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads";
        log.info(logBuilder);

        channelService.validateUserInChannel(channelId, userDetails.getUser());

        return threadService.getAllThreadsInChannel(channelId, pageDto);
    }

    /**
     * 대상 쓰레드를 조회
     * [GET] /api/channels/{channelId}/threads/{threadId}
     *
     * @param channelId 대상 채널
     * @param threadId  대상 쓰레드
     * @return ThreadResponseDto
     */
    @GetMapping("{channelId}/threads/{threadId}")
    public ThreadResponseDto getThread(@PathVariable Long channelId,
                                       @PathVariable Long threadId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads/" + threadId;
        log.info(logBuilder);

        channelService.validateUserInChannel(channelId, userDetails.getUser());

        return threadService.getThread(threadId);
    }

    /**
     * 작성자가 대상 쓰레드를 수정
     * [PATCH] /api/threads/{threadId}
     *
     * @param threadId         대상 쓰레드 ID
     * @param threadRequestDto 요청 DTO (수정될 쓰레드 내용)
     * @param userDetails      요청자 정보
     */
    @PatchMapping("/threads/{threadId}")
    public ThreadResponseDto updateThread(@PathVariable Long threadId,
                                          @RequestBody ThreadRequestDto threadRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[PATCH] /api/threads/" + threadId;
        log.info(logBuilder);

        return threadService.editThread(threadId, threadRequestDto.getContent(), userDetails.getUser());
    }

    /**
     * 작성자가 대상 쓰레드를 삭제
     * [PATCH] /api/threads/{threadId}
     *
     * @param threadId    대상 쓰레드 ID
     * @param userDetails 요청자 정보
     */
    @PutMapping("/threads/{threadId}")
    public void disableThread(@PathVariable Long threadId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[PUT] /api/threads/" + threadId;
        log.info(logBuilder);

        threadService.disableThread(threadId, userDetails.getUser());
    }

}
