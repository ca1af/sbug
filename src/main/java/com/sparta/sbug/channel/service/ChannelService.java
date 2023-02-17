package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.user.entity.User;

public interface ChannelService {

    /**
     * 채널 ID를 이용해서 일치하는 채널 데이터를 찾아 반환하는 메서드
     *
     * @param channelId 채널 ID
     * @return Channel
     */
    Channel getChannelById(Long channelId);

    /**
     * 채널을 만드는 메서드
     *
     * @param user        요청자
     * @param channelName 채널 이름
     * @return Channel
     */
    Channel createChannel( String channelName);

    /**
     * 채널 이름을 수정하는 메서드
     *
     * @param channelId   채널 ID
     * @param user        요청자
     * @param channelName 채널 이름
     */
    void updateChannelName(Long channelId, User user, String channelName);

    /**
     * 채널 삭제
     *
     * @param channelId 삭제할 채널 ID
     * @param user      요청자
     */
    void deleteChannel(Long channelId, User user);

    /**
     * Thread 생성
     *
     * @param channelId : 생성할 채널 ID
     * @param requestContent : thread 내용
     * @param user      : 요청자
     */
    void createThread(Long channelId, String requestContent, User user);
}
