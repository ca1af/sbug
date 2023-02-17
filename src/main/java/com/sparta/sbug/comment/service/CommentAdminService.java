package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.entity.Comment;

public interface CommentAdminService {
    void disableComment(Long commentId);

    void disableCommentByThreadId(Long threadId);
}
