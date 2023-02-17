package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserService;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class UserChannelUpperLayerServiceImpl implements UserChannelUpperLayerService {
    private final ChannelService channelService;
    private final UserChannelService userChannelService;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getChannelsByUserId(Long userId) {
        List<UserChannel> UserChannels = userChannelService.getUserChannelsByUserId(userId);
        List<Channel> channels = new ArrayList<>();
        for (UserChannel userChannel : UserChannels) {
            channels.add(userChannel.getChannel());
        }
        return channels.stream().map(ChannelResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createChannelAndUserChannelForRequester(User user, String channelName) {
        Channel channel = channelService.createChannel(channelName);
        userChannelService.createUserChannel(user, channel);
    }

    @Override
    @Transactional
    public void deleteChannelAndDependentUserChannel(User user, Long channelId) {
        userChannelService.deleteUserChannelsAboutChannel(channelId);
        channelService.deleteChannel(channelId, user);
    }

    @Override
    @Transactional
    public void inviteUser(User user, Long channelId, String email) {
        if (userChannelService.isUserJoinedByChannel(user, channelId)) {
            Channel channel = channelService.getChannelById(channelId);
            User invitedUser = userService.getUser(email);
            userChannelService.createUserChannel(invitedUser, channel);
        } else {
            throw new IllegalArgumentException("요청자가 채널에 속해있지 않습니다.");
        }
    }

    @Override
    @Transactional
    public void exitChannel(User user, Long id) {
        Channel channel = channelService.getChannelById(id);
        // 요청자가 채널에 속해있는지 확인하는 과정은 아래 메서드 안에서 진행됩니다.
        userChannelService.deleteUserChannelByUserAndChannel(user, channel);
    }

//    @Override
//    @Transactional
//    public void kickUser(User admin, Long id, String email) {
//        Channel channel = channelService.getChannelById(id);
//        if (channel.getAdminEmail().equals(admin.getEmail())) {
//            throw new IllegalArgumentException("권한이 없습니다.");
//        }
//
//        User user = userService.getUser(email);
//        userChannelService.deleteUserChannelByUserAndChannel(user, channel);
//    }

}
