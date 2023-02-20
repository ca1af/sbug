package com.sparta.sbug.admin.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.service.ThreadAdminService;
import com.sparta.sbug.thread.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInquiryServiceImpl implements AdminInquiryService{
    private final ChannelService channelService;
    private final CommentService commentService;
    private final ThreadService threadService;
    @Override
    public Slice<CommentResponseDto> getAllComments(Long threadId, PageDto pageDto){
        return commentService.getAllCommentsInThread(threadId, pageDto);
    }
    @Override
    public Slice<ThreadResponseDto> getAllThreads(Long channelId, PageDto pageDto){
        return threadService.getAllThreadsInChannel(channelId, pageDto);
    }
    @Override
    public Slice<ChannelResponseDto> getAllChannels(PageDto pageDto){
        return channelService.getChannels(pageDto);
    }
}
