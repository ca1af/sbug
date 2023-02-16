package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ThreadService threadService;
    private final UserChannelService userChannelService;

    @Override
    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(
                () -> new IllegalArgumentException("채널이 없습니다")
        );
    }

    @Override
    @Transactional
    public Channel createChannel(User user, String channelName) {
        Channel channel = Channel.builder().adminEmail(user.getEmail()).channelName(channelName).build();
        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void updateChannelName(Long channelId, User user, String channelName) {
        Channel channel = getChannelById(channelId);
        validateUserIsChannelAdmin(channel, user);
        channel.updateChannelName(channelName);
    }

    @Override
    @Transactional
    public void deleteChannel(Long channelId, User user) {
        Channel channel = getChannelById(channelId);
        validateUserIsChannelAdmin(channel, user);
        channelRepository.delete(channel);
    }

    private static void validateUserIsChannelAdmin(Channel channel, User user) {
        if (!channel.getAdminEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("채널 관리자만 수정 할 수 있습니다.");
        }
    }

    // Thread 생성
    @Override
    @Transactional
    public void createThread(Long channelId, String requestContent, User user) {
        Channel channel = getChannelById(channelId);
        if (!userChannelService.isUserJoinedByChannel(user, channel)) {
            throw new IllegalArgumentException("유저가 채널에 속해있지 않습니다. 쓰레드 생성권한이 없습니다.");
        }
        threadService.createThread(channel, requestContent, user);
    }
}
