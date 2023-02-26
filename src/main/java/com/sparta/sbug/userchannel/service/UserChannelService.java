package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface UserChannelService {

    // CRUD

    /**
     * 채널을 생성하고 요청자와 그 채널과 요청자에 대한 사용자-채널 데이터도 생성
     *
     * @param user        요청자
     * @param channelName 생성할 채널 이름
     * @return ChannelResponseDto
     */
    ChannelResponseDto createChannel(User user, String channelName);

    /**
     * 채널에 유저를 초대하는 메서드
     * 요청자가 채널에 속해있는지 확인하고 사용자를 초대합니다.
     *
     * @param user      요청자
     * @param channelId 채널
     * @param email     초대할 사용자
     */
    void inviteUser(User user, Long channelId, String email);

    /**
     * 유저가 속해있는 모든 채널을 반환하는 메서드
     *
     * @param userId 사용자
     * @return List&lt;ChannelResponseDto&gt;
     */
    List<ChannelResponseDto> getChannelsByUserId(Long userId);

    /**
     * 채널에서 나가는 메서드
     *
     * @param user      요청자
     * @param channelId 대상 채널
     */
    void exitChannel(User user, Long channelId);

    /**
     * 채널 운영자가 사용자를 강퇴하는 메서드
     * 요청자가 운영자인지 확인하고 사용자를 강퇴합니다.
     *
     * @param channelId 채널
     * @param email     강퇴할 사용자
     */
    void kickUser(Long channelId, String email);

    /**
     * 채널 삭제 ( 논리 삭제 )
     *
     * @param channelId 대상 채널
     */
    void disableChannel(Long channelId);

    /**
     * 회원 탈퇴 ( 회원 논리 삭제 )
     *
     * @param user 대상 유저
     */
    void disableUser(User user);

    // 유저-채널 존재 검증

    /**
     * 특정 사용자가 채널에 속해있는지 확인합니다.
     *
     * @param user      사용자
     * @param channelId 채널
     * @return
     */
    boolean isUserJoinedByChannel(User user, Long channelId);

    /**
     * Thread 생성
     *
     * @param channelId      : 생성할 채널 ID
     * @param requestContent : thread 내용
     * @param user           : 요청자
     */
    ThreadResponseDto createThread(Long channelId, String requestContent, User user);
}
