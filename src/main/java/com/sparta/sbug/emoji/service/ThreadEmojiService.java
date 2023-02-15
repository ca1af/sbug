package com.sparta.sbug.emoji.service;

import com.sparta.sbug.user.entity.User;

public interface ThreadEmojiService {

    String createThreadEmoji(String emojiType, User user, Long threadId);
    String deleteThreadEmoji(String emojiType, User user, Long threadId);

}
