package com.sparta.sbug.thread.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface ThreadService {

    /**
     * 쓰레드 ID로 쓰레드 객체를 찾아 반환
     *
     * @param threadId 대상 쓰레드 ID
     * @return Thread
     */
    Thread findThreadById(Long threadId);

    /**
     * 대상 채널에 새로운 쓰레드를 작성
     *
     * @param channel        대상 채널 ID
     * @param requestContent 쓰레드 내용
     * @param user           요청자(=작성자)
     */
    ThreadResponseDto createThread(Channel channel, String requestContent, User user);

    /**
     * 모든 쓰레드를 조회하는 메서드 ( 관리자 관리 용도 )
     *
     * @param channelId
     * @param pageDto
     * @return
     */
    Page<ThreadResponseDto> getAllThreads(Long channelId, PageDto pageDto);

    /**
     * 대상 쓰레드를 수정
     *
     * @param threadId       대상 쓰레드 ID
     * @param requestContent 수정될 쓰레드 내용
     * @param user           요청자
     */
    ThreadResponseDto editThread(Long threadId, String requestContent, User user);

    /**
     * 대상 쓰레드를 삭제
     *
     * @param threadId 대상 쓰레드 ID
     * @param user     요청자
     */
    void disableThread(Long threadId, User user);

    /**
     * 대상 채널의 모든 쓰레드를 조회
     *
     * @param channelId 대상 채널 ID
     * @param pageDto   페이징 DTO
     * @return List&lt;ThreadResponseDto&gt;
     */
    Slice<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto);

    /**
     * 대상 쓰레드를 조회
     *
     * @param threadId 대상 쓰레드
     * @return ThreadResponseDto
     */
    ThreadResponseDto getThread(Long threadId);

    /**
     * 쓰레드 자동 삭제
     * - 3개월에 한 번, 1일 새벽 5시에 삭제
     * - 비활성화된지 3개월이 지난 쓰레드들만 삭제
     */
    void deleteThreadsOnSchedule();
    /**
     *
     */
    boolean existsThreadById(Long threadId);

    // Disable //
    void disableThreadsByChannelId(Long channelId);

    void disableThread(Long threadId);
}
