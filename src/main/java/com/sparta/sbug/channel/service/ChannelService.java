package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

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
    Channel createChannel(User user, String channelName);

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
     * @param channelId      : 생성할 채널 ID
     * @param requestContent : thread 내용
     * @param user           : 요청자
     */
    ThreadResponseDto createThread(Long channelId, String requestContent, User user);

    /**
     * 요청자가 대상 채널에 가입되어 있는지 확인하고 채널 엔터티를 반환합니다.
     *
     * @param channelId 대상 채널
     * @param user      요청자
     * @return Channel
     */
    @Transactional(readOnly = true)
    Channel validateUserInChannel(Long channelId, User user);
}
