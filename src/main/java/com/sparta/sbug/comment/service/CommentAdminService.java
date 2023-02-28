package com.sparta.sbug.comment.service;

public interface CommentAdminService {
    void disableComment(Long commentId);

    void disableCommentByThreadId(Long threadId);

    void disableCommentByChannelId(Long channelId);
}
