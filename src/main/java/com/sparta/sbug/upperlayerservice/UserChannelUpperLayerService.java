package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface UserChannelUpperLayerService {

    /**
     * 유저가 속해있는 모든 채널을 반환하는 메서드
     *
     * @param userId : 사용자
     */
    List<ChannelResponseDto> getChannelsByUserId(Long userId);

    /**
     * 채널을 생성하고 요청자와 그 채널과 요청자에 대한 사용자-채널 데이터도 생성합니다.
     *
     * @param user        : 요청자
     * @param channelName : 생성할 채널 이름
     */
    void createChannelAndUserChannelForRequester(User user, String channelName);

    /**
     * 채널을 삭제하고 그에 종속된 사용자-채널 데이터도 삭제합니다.
     *
     * @param user      : 요청자
     * @param channelId : 삭제할 채널 ID
     */
    void deleteChannelAndDependentUserChannel(User user, Long channelId);

    /**
     * 채널에 유저를 초대하는 메서드
     * 요청자가 채널에 속해있는지 확인하고 사용자를 초대합니다.
     *
     * @param user      : 요청자
     * @param channelId : 채널
     * @param email     : 초대할 사용자
     */
    void inviteUser(User user, Long channelId, String email);

    /**
     * 채널에서 나가는 메서드
     *
     * @param user : 요청자
     * @param id   : 대상 채널
     */
    void exitChannel(User user, Long id);

    /**
     * 채널 운영자가 사용자를 강퇴하는 메서드
     * 요청자가 운영자인지 확인하고 사용자를 강퇴합니다.
     *
     * @param user  : 운영자(=요청자)
     * @param id    : 채널
     * @param email : 강퇴할 사용자
     */
    void kickUser(User user, Long id, String email);
}
