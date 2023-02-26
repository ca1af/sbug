package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.sparta.sbug.common.exceptions.ErrorCode.*;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ChannelServiceImpl implements ChannelService {

    /**
     * 레포지토리
     */
    private final ChannelRepository channelRepository;

    /**
     * 하위 레이어 데이터 서비스 - 쓰레드 서비스
     */
    private final ThreadService threadService;

    /**
     * 하위 레이어 데이터 서비스 - 유저-채널 서비스
     */
    private final UserChannelService userChannelService;


    // CRUD //
    @Override
    @Transactional
    public Channel createChannel(String channelName) {
        Channel channel = Channel.builder().channelName(channelName).build();
        return channelRepository.save(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(
                () -> new CustomException(CHANNEL_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChannelResponseDto> getAllChannelsToPage(PageDto pageDto) {
        Page<Channel> channels = channelRepository.findAllByInUseIsTrue(pageDto.toPageable());
        return channels.map(ChannelResponseDto::of);
    }

    @Override
    @Transactional
    public void updateChannelName(Long channelId, String channelName) {
        Channel channel = getChannelById(channelId);
        channel.updateChannelName(channelName);
    }

    @Override
    @Transactional
    public void deleteChannel(Long channelId) {
        Channel channel = getChannelById(channelId);
        channelRepository.delete(channel);
    }

    // Thread Create //
    @Override
    @Transactional
    public ThreadResponseDto createThread(Long channelId, String requestContent, User user) {
        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        Channel channel = validateUserInChannel(channelId, user);
        return threadService.createThread(channel, requestContent, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Channel validateUserInChannel(Long channelId, User user) {
        if (!userChannelService.isUserJoinedByChannel(user, channelId)) {
            throw new CustomException(USER_CHANNEL_FORBIDDEN);
        }

        return getChannelById(channelId);
    }

    // Auto Delete //
    @Transactional
    @Override
    @Scheduled(cron = "0 0 5 1 1/3 ? *")
    public void deleteChannelsOnSchedule() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(3);
        channelRepository.deleteChannels(localDateTime);
    }

    // Disable //
    @Override
    @Transactional
    public void disableChannel(Long channelId) {
        threadService.disableThreadsByChannelId(channelId);
        userChannelService.disableUserChannelByChannelAbsence(channelId);
        channelRepository.disableChannelById(channelId);
    }
}
