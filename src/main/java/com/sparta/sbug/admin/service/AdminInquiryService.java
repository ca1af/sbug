package com.sparta.sbug.admin.service;


import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import org.springframework.data.domain.Slice;

public interface AdminInquiryService {
    public Slice<CommentResponseDto> getAllComments(Long threadId, PageDto pageDto);

    public Slice<ThreadResponseDto> getAllThreads(Long channelId, PageDto pageDto);

    public Slice<ChannelResponseDto> getAllChannels(PageDto pageDto);

}
