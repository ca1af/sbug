package com.sparta.sbug.thread.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface ThreadService {

    Thread getThread(Long threadId);

    String createThread(Long channelId, ThreadRequestDto threadRequestDto, User user);

    String editThread(Long ThreadId, ThreadRequestDto threadRequestDto, User user);

    String deleteThread(Long threadId, Long userId);

    Thread findThreadById(Long threadId);

    List<CommentResponseDto> getComments(Long threadId);

}
