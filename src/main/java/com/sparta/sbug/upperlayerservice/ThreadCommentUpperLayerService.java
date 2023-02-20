package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.user.entity.User;

public interface ThreadCommentUpperLayerService {

    /**
     * 대상 쓰레드에 댓글을 작성하는 메서드
     *
     * @param threadId 대상 쓰레드 ID
     * @param content  댓글 내용
     * @param user     요청자(=작성자)
     */
    CommentResponseDto createComment(Long threadId, String content, User user);

    /**
     * 대상 댓글의 내용을 수정하는 메서드
     *
     * @param commentId 대상 댓글 ID
     * @param content   수정될 내용
     * @param user      요청자
     */
    void updateComment(Long commentId, String content, User user);

    /**
     * 대상 댓글을 삭제하는 메서드
     *
     * @param commentId 대상 댓글 ID
     * @param user      요청자
     */
    void deleteComment(Long commentId, User user);
}
