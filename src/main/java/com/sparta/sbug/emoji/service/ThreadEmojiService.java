package com.sparta.sbug.emoji.service;


import com.sparta.sbug.emoji.dto.EmojiRequestDto;
import com.sparta.sbug.user.entity.User;

public interface ThreadEmojiService {

    String createThreadEmoji(Long threadId, EmojiRequestDto emojiRequestDto, User user);
    String deleteThreadEmoji(Long emojiId, User user);

}
