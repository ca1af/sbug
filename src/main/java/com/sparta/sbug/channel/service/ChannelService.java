package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    Channel getChannel(Long channelId);
    String createChannel(User user, ChannelRequestDto dto);
    String inviteUser(User user, Channel channel, String email);
    void updateChannelName(Channel channel, User user, ChannelRequestDto dto);
    void deleteChannel(Channel channel, User user);
    public List<ThreadResponseDto> getThreads(Channel channel);
}
