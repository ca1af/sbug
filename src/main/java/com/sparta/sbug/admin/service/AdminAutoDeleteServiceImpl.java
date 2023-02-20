package com.sparta.sbug.admin.service;

import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.thread.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class AdminAutoDeleteServiceImpl implements AdminAutoDeleteService{
    private final ChannelService channelService;
    private final ThreadService threadService;
    private final CommentService commentService;
    @Override
    @Scheduled(cron = "* * 5 * * *")
    public void deleteThreads() {
        threadService.autoDelete();
    }

    @Override
    @Scheduled(cron = "* * 5 * * *")
    public void deleteComments() {
        commentService.autoDelete();
    }

    @Override
    @Scheduled(cron = "* * 5 * * *")
    public void deleteChannels() {
        channelService.autoDelete();
    }
}
