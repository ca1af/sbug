package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelAdminServiceImpl implements ChannelAdminService{
    private final ChannelRepository channelRepository;
    @Override
    public void disableChannel(Long channelId) {
        channelRepository.disableChannelById(channelId);
    }
}
