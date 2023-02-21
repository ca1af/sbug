package com.sparta.sbug.admin.controller;

import com.sparta.sbug.admin.service.AdminInquiryService;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminInquiryController {
    private final AdminInquiryService adminInquiryService;

    @GetMapping("admin/{channelId}/threads/{threadId}/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public Slice<CommentResponseDto> getAllCommentsInThreadAdmin(
            @PathVariable Long channelId,
            @PathVariable Long threadId,
            @ModelAttribute PageDto pageDto) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads/" + threadId + "/comments";

        log.info(logBuilder);
        return adminInquiryService.getAllComments(threadId, pageDto);
    }

    @GetMapping("admin/{channelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Slice<ThreadResponseDto> getThreads(@PathVariable Long channelId,
                                        @ModelAttribute PageDto pageDto) {

        String logBuilder = "[GET] /api/channels/" + channelId + "/threads/";
        log.info(logBuilder);

        return adminInquiryService.getAllThreads(channelId, pageDto);
    }

    @GetMapping("channels")
    @PreAuthorize("hasRole('ADMIN')")
    public Slice<ChannelResponseDto> channels(@ModelAttribute PageDto pageDto){
        return adminInquiryService.getAllChannels(pageDto);
    }
}
