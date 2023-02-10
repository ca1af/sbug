package com.sparta.sbug.emoji.service;

import com.sparta.sbug.user.entity.User;

public interface CommentEmojiService {
    String createCommentEmoji(Long commentId, String emojiType, User user);
    String deleteCommentEmoji(Long emojiId, User user);
}