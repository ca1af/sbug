package com.sparta.sbug.emoji.service;

import com.sparta.sbug.emoji.dto.EmojiRequestDto;
import com.sparta.sbug.user.entity.User;

public interface CommentEmojiService {
    String createCommentEmoji(Long commentId, EmojiRequestDto emojiRequestDto, User user);
    String deleteCommentEmoji(Long emojiId, User user);
}