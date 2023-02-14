package com.sparta.sbug.emoji.service;

import com.sparta.sbug.user.entity.User;

public interface CommentEmojiService {

    // CommentEmoji 생성
    String createCommentEmoji(String emojiType, User user, Long commentId);

    String deleteCommentEmoji(String emojiType, User user, Long emojiId);
}