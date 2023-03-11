package com.sparta.sbug.thread.controller;

import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.common.exceptions.ErrorCode;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.repository.query.ThreadSearchCond;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.userchannel.service.UserChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.sbug.common.exceptions.ErrorCode.USER_THREAD_FORBIDDEN;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api")
public class ThreadController {

    private final UserChannelService userChannelService;
    private final ThreadService threadService;

    /**
     * 대상 채널에 쓰레드를 생성
     * [POST] /api/channels/{id}/threads
     *
     * @param channelId        대상 채널 ID
     * @param threadRequestDto 요청 DTO (쓰레드 내용)
     * @param userDetails      요청자 정보
     */
    @PostMapping("/channels/{channelId}/threads")
    public ThreadResponseDto createThread(
            @PathVariable Long channelId, @RequestBody @Valid ThreadRequestDto threadRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[POST] api/channels/" + channelId + "/threads";
        log.info(logBuilder);

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(ErrorCode.USER_CHANNEL_FORBIDDEN);
        }

        return userChannelService.createThread(channelId, threadRequestDto.getContent(), userDetails.getUser());
    }

    /**
     * 대상 채널 아래 모든 쓰레드를 조회
     * [GET] /api/channels/{channelId}/threads
     *
     * @param channelId 대상 채널
     * @param pageDto   페이징 DTO
     * @return List&lt;ThreadResponseDto&gt;
     */
    @GetMapping("/channels/{channelId}/threads")
    public Slice<ThreadResponseDto> getAllThreadsInChannel(@PathVariable Long channelId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @ModelAttribute PageDto pageDto) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads";
        log.info(logBuilder);

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
             throw new CustomException(ErrorCode.USER_CHANNEL_FORBIDDEN);
        }

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
    @GetMapping("/channels/{channelId}/threads/{threadId}")
    public ThreadResponseDto getThread(@PathVariable Long channelId,
                                       @PathVariable Long threadId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads/" + threadId;
        log.info(logBuilder);

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(ErrorCode.USER_THREAD_FORBIDDEN);
        }

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
    public void updateThread(@PathVariable Long threadId,
                                          @RequestBody ThreadRequestDto threadRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        threadService.editThread(threadId, threadRequestDto.getContent(), userDetails.getUser());
    }

    /**
     * 작성자가 대상 쓰레드를 삭제
     * [PUT] /api/threads/{threadId}
     *
     * @param threadId    대상 쓰레드 ID
     * @param userDetails 요청자 정보
     */
    @PutMapping("/threads/{threadId}")
    public void disableThread(@PathVariable Long threadId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        threadService.disableThread(threadId, userDetails.getUser());
    }
    @GetMapping("/threads/search")
    public List<ThreadResponseDto> searchByCond(@RequestBody ThreadSearchCond threadSearchCond){
        return threadService.findThreadBySearchCondition(threadSearchCond);
    }

    /**
     * 쓰레드 이모지 반응을 생성하거나 이미 동일한 반응이 존재한다면 삭제
     * [POST] /api/channels/{channelId}/threads/{threadId}/emojis
     *
     * @param channelId   채널 ID
     * @param threadId    쓰레드 ID
     * @param emojiType   이모지 반응 타입(열거형)
     * @param userDetails 요청자 정보
     */
    @PostMapping("/channels/{channelId}/threads/{threadId}/emojis")
    public boolean reactThreadEmoji(
            @PathVariable Long channelId,
            @PathVariable Long threadId,
            @RequestBody String emojiType,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        if (!userChannelService.isUserJoinedByChannel(userDetails.getUser(), channelId)) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }

        return threadService.reactThreadEmoji(emojiType, userDetails.getUser(), threadId);
    }
}
