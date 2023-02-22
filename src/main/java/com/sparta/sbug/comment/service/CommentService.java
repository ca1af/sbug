package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface CommentService {

    /**
     * 대상 쓰레드에 달린 모든 댓글을 조회하는 메서드
     *
     * @param threadId 대상 쓰레드 ID
     * @param pageDto  페이징 DTO
     * @return List&lt;CommentResponseDto&gt;
     */
    Slice<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto);

    /**
     * 대상 쓰레드 밑에 댓글을 생성하는 메서드
     *
     * @param thread  대상 쓰레드
     * @param content 댓글 내용
     * @param user    요청자
     */
    CommentResponseDto createComment(Thread thread, String content, User user);

    /**
     * 대상 댓글을 수정하는 메서드
     *
     * @param comment 대상 댓글
     * @param content 수정할 내용
     */
    void updateComment(Comment comment, String content);

    /**
     * 대상 댓글을 삭제하는 메서드
     *
     * @param comment 대상 댓글
     */
    void deleteComment(Comment comment);

    /**
     * 댓글 엔티티를 조회하는 메서드
     *
     * @param commentId 대상 댓글 ID
     * @return Comment
     */
    Comment getComment(Long commentId);

    void autoDelete();

    boolean existCommentById(Long commentId);
}
