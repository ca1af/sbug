package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Slice;

public interface CommentService {


    // CRUD

    /**
     * 대상 쓰레드 밑에 댓글을 생성하는 메서드
     *
     * @param thread  대상 쓰레드
     * @param content 댓글 내용
     * @param user    요청자
     */
    CommentResponseDto createComment(Thread thread, String content, User user);

    /**
     * 댓글 엔티티를 조회하는 메서드
     *
     * @param commentId 대상 댓글 ID
     * @return Comment
     */
    Comment getComment(Long commentId);

    /**
     * 대상 쓰레드에 달린 모든 댓글을 조회하는 메서드
     *
     * @param threadId 대상 쓰레드 ID
     * @param pageDto  페이징 DTO
     * @return List&lt;CommentResponseDto&gt;
     */
    Slice<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto);

    /**
     * 대상 코멘트를 수정하는 메서드
     *
     * @param commentId 대상 코멘트
     * @param content   수정할 내용
     * @param user      요청자
     */
    void updateComment(Long commentId, String content, User user);

    /**
     * 대상 코멘트를 삭제하는 메서드
     *
     * @param commentId 대상 코멘트
     * @param user      요청자
     */
    void disableComment(Long commentId, User user);

    // 유저 권한 검증

    /**
     * 유저가 대상 댓글을 수정/삭제할 수 있는 권한을 가졌는지 확인하고
     * 권한을 가졌다면 대상 댓글 엔티티를 반환합니다.
     *
     * @param commentId 대상 댓글
     * @param user      사용자
     * @return Comment
     */
    Comment validateUserAuth(Long commentId, User user);

    /**
     * 코멘트 자동 삭제
     * - 3개월에 한 번, 1일 새벽 5시에 삭제
     * - 비활성화된지 3개월이 지난 코멘트들만 삭제
     */
    void deleteCommentsOnSchedule();

    // Disable //

    /**
     * 관리자가 채널을 비활성화 하려는 요청을 했을 때
     * 대상 채널 아래에 있는 코멘트들도 비활성화 되기 위해 실행됩니다.
     *
     * @param channelId 대상 채널
     */
    void disableCommentByChannelId(Long channelId);

    /**
     * 관리자가 쓰레드를 비활성화 하려는 요청을 했을 때
     * 대상 쓰레드 아래에 있는 코멘트들도 비활성화 되기 위해 실행됩니다.
     *
     * @param threadId 대상 쓰레드
     */
    void disableCommentByThreadId(Long threadId);

    /**
     * 관리자가 코멘트를 비활성화 하려는 요청을 했을 때 실행됩니다.
     *
     * @param commentId 대상 코멘트
     */
    void disableCommentByAdmin(Long commentId);

    /**
     * 코멘트 ID로 코멘트를 찾고 이모지 코멘트 서비스에서 코멘트 반응 메소드를 호출합니다.
     *
     * @param emojiType 이모지 종류
     * @param user      반응한 유저
     * @param commentId 대상 댓글
     */
    boolean reactCommentEmoji(String emojiType, User user, Long commentId);
}
