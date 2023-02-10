package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.user.entity.User;

public interface ChannelService {

    /**
     * 채널 ID를 이용해서 일치하는 채널 데이터를 찾아 반환하는 메서드
     *
     * @param channelId : 채널 ID
     */
    Channel getChannelById(Long channelId);

    /**
     * 채널을 만드는 메서드
     *
     * @param user        : 요청자
     * @param channelName : 채널 이름
     */
    Channel createChannel(User user, String channelName);

    /**
     * 채널 이름을 수정하는 메서드
     *
     * @param channelId   : 채널 ID
     * @param user        : 요청자
     * @param channelName : 채널 이름
     */
    void updateChannelName(Long channelId, User user, String channelName);

    /**
     * 채널 삭제
     *
     * @param user      : 요청자
     * @param channelId : 삭제할 채널 ID
     */
    void deleteChannel(User user, Long channelId);
}
