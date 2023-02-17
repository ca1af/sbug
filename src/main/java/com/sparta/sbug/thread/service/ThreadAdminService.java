package com.sparta.sbug.thread.service;

public interface ThreadAdminService {
    void disableThread(Long threadId);
    void disableThreadsByChannelId(Long channelId);
}
