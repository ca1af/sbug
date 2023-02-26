package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ChannelService {

    // CRUD //

    /**
     * 새로운 채널을 만듦
     *
     * @param channelName 채널 이름
     * @return Channel
     */
    Channel createChannel(String channelName);

    /**
     * 채널 ID를 이용해서 일치하는 채널 데이터를 조회
     *
     * @param channelId 채널 ID
     * @return Channel
     */
    Channel getChannelById(Long channelId);

    /**
     * 모든 채널을 조회
     *
     * @param pageDto 페이징 정보
     * @return Page&lt;ChannelResponseDto&gt;
     */
    Page<ChannelResponseDto> getAllChannelsToPage(PageDto pageDto);

    /**
     * 채널 이름을 수정
     *
     * @param channelId   채널 ID
     * @param channelName 채널 이름
     */
    void updateChannelName(Long channelId, String channelName);

    /**
     * 채널 삭제
     *
     * @param channelId 삭제할 채널 ID
     */
    void deleteChannel(Long channelId);

    // Create Thread //

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

    // Delete On Schedule

    /**
     * 채널 자동 삭제
     * - 3개월에 한 번, 1일 새벽 5시에 삭제
     * - 비활성화된지 3개월이 지난 채널들만 삭제
     */
    void deleteChannelsOnSchedule();

    // Disable
    void disableChannel(Long channelId);

}
