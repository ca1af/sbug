package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.user.entity.User;

public interface ThreadCommentUpperLayerService {

    String createComment(Long threadId, String content, User user);

    String updateComment(Long commentId, String content, User user);

    String deleteComment(Long commentId, User user);
}
