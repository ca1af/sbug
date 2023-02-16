package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.enttiy.UserChannel;

import java.util.List;

public interface UserChannelService {
    /**
     * 유저가 속해있는 모든 채널을 반환하는 메서드
     *
     * @param userId 사용자
     * @return List&lt;UserChannel&gt;
     */
    List<UserChannel> getUserChannelsByUserId(Long userId);

    /**
     * 사용자-채널을 만드는 메서드
     *
     * @param user    사용자
     * @param channel 채널
     * @return Long
     */
    Long createUserChannel(User user, Channel channel);

    /**
     * 채널에 종속된 사용자-채널을 삭제합니다.
     *
     * @param channelId 대상 채널
     */
    void deleteUserChannelsAboutChannel(Long channelId);

    /**
     * 사용자에 종속된 사용자-채널을 삭제합니다.
     *
     * @param userId 사용자
     */
    void deleteUserChannelsAboutUser(Long userId);

    /**
     * 특정 사용자와 채널에 대한 사용자-채널을 삭제합니다.
     *
     * @param user    사용자
     * @param channel 채널
     */
    void deleteUserChannelByUserAndChannel(User user, Channel channel);

    /**
     * 특정 사용자가 채널에 속해있는지 확인합니다.
     *
     * @param user    사용자
     * @param channel 채널
     * @return boolean
     */
    boolean isUserJoinedByChannel(User user, Channel channel);
}
