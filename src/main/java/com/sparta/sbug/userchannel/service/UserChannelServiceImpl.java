package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class UserChannelServiceImpl implements UserChannelService {

    private final UserChannelRepository userChannelRepository;

//    // 1. 유저가 채널에서 나간 경우
//    @Override
//    public void leaveChannel(Long channelId, Long userId) {
//
//    }
//    // 4. 유저가 탈퇴한 경우
//    @Override
//    public void disableUserChannelByUserAbsence(Long userId) {
//
//    }

    @Override
    public List<UserChannel> getUserChannelsByUserId(Long userId) {
        return userChannelRepository.findAllChannelByUserIdAndInUseIsTrue(userId);
    }

    @Override
    @Transactional
    public Long createUserChannel(User user, Channel channel) {
        if (userChannelRepository.findByUserAndChannelAndInUseIsTrue(user, channel).isPresent()) {
            throw new IllegalArgumentException("중복된 리소스가 존재합니다.");
        }
        UserChannel userChannel = UserChannel.builder().user(user).channel(channel).build();
        return userChannelRepository.save(userChannel).getId();
    }

    @Override
    @Transactional
    public void deleteUserChannelsAboutChannel(Long channelId) {
        userChannelRepository.deleteAllByChannelId(channelId);
    }

    @Override
    public void deleteUserChannelsAboutUser(Long userId) {
        userChannelRepository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteUserChannelByUserAndChannel(User user, Channel channel) {
        UserChannel userChannel = userChannelRepository.findByUserAndChannelAndInUseIsTrue(user, channel).orElseThrow(
                () -> new NoSuchElementException("찾는 유저 채널이 없습니다.")
        );
        userChannelRepository.delete(userChannel);
    }

    @Override
    public boolean isUserJoinedByChannel(User user, Long channelId) {
        return userChannelRepository.existsByUserAndChannelIdAndInUseIsTrue(user, channelId);
    }

    // Disable //
    @Override
    public void disableUserChannelByChannelAbsence(Long channelId) {
        userChannelRepository.disableAllUserChannelByChannelIdAndInUse(channelId);
    }
}
