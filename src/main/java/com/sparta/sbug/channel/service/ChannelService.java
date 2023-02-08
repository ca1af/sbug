package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    Channel getChannel(Long channelId);
    String createChannel(User user, String channelName);
    String inviteUser(User user, Channel channel, String email);
    void updateChannelName(Channel channel, User user, String channelName);
    public void deleteChannel(User user, Long id);
    List<ThreadResponseDto> getThreads(Long id);
}
