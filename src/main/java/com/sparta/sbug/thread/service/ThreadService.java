package com.sparta.sbug.thread.service;

import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.user.entity.User;

public interface ThreadService {
    String createThread(Long channelId, ThreadRequestDto threadRequestDto, User user);

    String editThread(Long ThreadId, ThreadRequestDto threadRequestDto, User user);

    String deleteThread(Long threadId, User user);

}
