package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserService;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.sbug.common.exceptions.ErrorCode.*;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class UserChannelServiceImpl implements UserChannelService {

    private final UserChannelRepository userChannelRepository;
    private final ChannelService channelService;
    private final UserService userService;

    // CRUD

    @Override
    public ChannelResponseDto createChannel(User user, String channelName) {
        Channel channel = channelService.createChannel(channelName);
        if (userChannelRepository.existsByUserAndChannelAndInUseIsTrue(user, channel)) {
            throw new CustomException(DUPLICATE_USER_CHANNEL);
        }
        UserChannel userChannel = UserChannel.builder().user(user).channel(channel).build();
        userChannelRepository.save(userChannel);
        return ChannelResponseDto.of(channel);
    }

    @Override
    public void inviteUser(User user, Long channelId, String email) {
        if (!isUserJoinedByChannel(user, channelId)) {
            throw new CustomException(USER_CHANNEL_FORBIDDEN);
        }

        Channel channel = channelService.getChannelById(channelId);
        User invitedUser = userService.getUser(email);
        UserChannel userChannel = UserChannel.builder().user(invitedUser).channel(channel).build();
        userChannelRepository.save(userChannel);
    }

    @Override
    public List<ChannelResponseDto> getChannelsByUserId(Long userId) {
        List<UserChannel> UserChannels = userChannelRepository.findAllChannelByUserIdAndInUseIsTrue(userId);
        List<Channel> channels = new ArrayList<>();
        for (UserChannel userChannel : UserChannels) {
            channels.add(userChannel.getChannel());
        }
        return channels.stream().map(ChannelResponseDto::of).collect(Collectors.toList());
    }

    @Override
    public void exitChannel(User user, Long channelId) {
        UserChannel userChannel = userChannelRepository.findByUserAndChannelIdAndInUseIsTrue(user, channelId).orElseThrow(
                () -> new CustomException(USER_CHANNEL_NOT_FOUND)
        );
        userChannelRepository.delete(userChannel);
    }

    @Override
    public void kickUser(Long channelId, String email) {

    }

    @Override
    public void disableChannel(Long channelId) {
        userChannelRepository.disableAllUserChannelByChannelIdAndInUse(channelId);
        channelService.disableChannel(channelId);
    }

    @Override
    public void disableUser(User user) {


    }

    // 유저-채널 존재 검증
    @Override
    public boolean isUserJoinedByChannel(User user, Long channelId) {
        return userChannelRepository.existsByUserAndChannelIdAndInUseIsTrue(user, channelId);
    }

    // Thread 생성 //
    @Override
    @Transactional
    public ThreadResponseDto createThread(Long channelId, String requestContent, User user) {
        if (isUserJoinedByChannel(user, channelId)) {
            throw new CustomException(USER_CHANNEL_FORBIDDEN);
        }

        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        return channelService.createThread(channelId, requestContent, user);
    }

}
