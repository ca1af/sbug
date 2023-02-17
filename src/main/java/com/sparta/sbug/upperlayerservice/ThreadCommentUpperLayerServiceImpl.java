package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ThreadCommentUpperLayerServiceImpl implements ThreadCommentUpperLayerService {

    private final ThreadService threadService;
    private final CommentService commentService;
    private final UserChannelService userChannelService;

    @Override
    @Transactional
    public void createComment(Long threadId, String content, User user) {
        Thread thread = threadService.findThreadById(threadId);
        if (!userChannelService.isUserJoinedByChannel(user, thread.getChannel().getId())) {
            throw new IllegalArgumentException("유저가 채널에 속해있지 않습니다. 권한이 없습니다");
        }

        commentService.createComment(thread, content, user);
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String content, User user) {
        Comment comment = validateUserAuth(commentId, user);
        commentService.updateComment(comment, content);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = validateUserAuth(commentId, user);
        commentService.deleteComment(comment);
    }

    /**
     * 유저가 대상 댓글을 수정/삭제할 수 있는 권한을 가졌는지 확인하고
     * 권한을 가졌다면 대상 댓글 엔티티를 반환합니다.
     *
     * @param commentId 대상 댓글
     * @param user      사용자
     * @return Comment
     */
    @Transactional
    public Comment validateUserAuth(Long commentId, User user) {
        Comment comment = commentService.getComment(commentId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정할 수 있는 권한이 없습니다.");
        }

        return comment;
    }
}
