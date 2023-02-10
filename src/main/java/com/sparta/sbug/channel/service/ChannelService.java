package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    Channel getChannel(Long channelId);
    void createChannel(User user, String channelName);
    String inviteUser(User user, Long id, String email);
    void updateChannelName(Long id, User user, String channelName);
    Long deleteChannel(User user, Long id);
    List<ThreadResponseDto> getThreads(Long id);
}
