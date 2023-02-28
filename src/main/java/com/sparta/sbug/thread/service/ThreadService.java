package com.sparta.sbug.thread.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.query.ThreadSearchCond;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ThreadService {

    // CRUD //

    /**
     * 대상 채널에 새로운 쓰레드를 작성
     *
     * @param channel        대상 채널 ID
     * @param requestContent 쓰레드 내용
     * @param user           요청자(=작성자)
     */
    ThreadResponseDto createThread(Channel channel, String requestContent, User user);

    /**
     * 대상 채널의 모든 쓰레드를 조회 ( 슬라이스, 유저 )
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
     * 대상 쓰레드를 수정
     *
     * @param threadId       대상 쓰레드 ID
     * @param requestContent 수정될 쓰레드 내용
     * @param user           요청자
     */
    ThreadResponseDto editThread(Long threadId, String requestContent, User user);

    /**
     * 대상 쓰레드를 비활성화 ( 논리 삭제 )
     *
     * @param threadId 대상 쓰레드 ID
     * @param user     요청자
     */
    void disableThread(Long threadId, User user);


    /**
     * 요청자가 대상 쓰레드를 수정 혹은 삭제할 수 있는 권한이 있는지 확인합니다.
     *
     * @param threadId 대상 쓰레드 ID
     * @param user     요청자
     * @return Thread
     */
    Thread validateUserAuth(Long threadId, User user);

    /**
     * 쓰레드 ID로 쓰레드 객체를 찾아 반환
     *
     * @param threadId 대상 쓰레드 ID
     * @return Thread
     */
    Thread findThreadById(Long threadId);

    // 코멘트 생성

    /**
     * 코멘트를 생성하기 위해서 실행됩니다.
     *
     * @param threadId 대상 쓰레드
     * @param content  코멘트 내용
     * @param user     요청자
     * @return CommentResponseDto
     */
    CommentResponseDto createComment(Long threadId, String content, User user);

    // Disable //

    /**
     * 관리자가 채널 비활성화 요청을 하면
     * 그 아래 종속된 모든 쓰레드들이 비활성화 되기 위해서 실행됩니다.
     *
     * @param channelId 대상 채널
     */
    void disableThreadsByChannelId(Long channelId);

    /**
     * 관리자가 쓰레드 비활성화 요청을 하면 실행됩니다.
     *
     * @param threadId 대상 쓰레드
     */
    void disableThread(Long threadId);

    /**
     * 쓰레드 자동 삭제
     * - 3개월에 한 번, 1일 새벽 5시에 삭제
     * - 비활성화된지 3개월이 지난 쓰레드들만 삭제
     */
    void deleteThreadsOnSchedule();

    /**
     * 동적 쿼리를 사용한 쓰레드 조회
     * @param threadSearchCond
     * @return
     */
    List<ThreadResponseDto> findThreadBySearchCondition(ThreadSearchCond threadSearchCond);
}
