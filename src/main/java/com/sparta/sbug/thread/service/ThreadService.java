package com.sparta.sbug.thread.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface ThreadService {

    Thread getThread(Long threadId);

    String createThread(Channel channel, String requestContent, User user);

    String editThread(Thread thread, String requestContent);

    String deleteThread(Thread thread);

    List<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto);

    Thread findThreadById(Long threadId);

}
