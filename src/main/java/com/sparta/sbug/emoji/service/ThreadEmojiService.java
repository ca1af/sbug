package com.sparta.sbug.emoji.service;

import com.sparta.sbug.user.entity.User;

public interface ThreadEmojiService {

    String createThreadEmoji(Long threadId,  String emojiType, User user);
    String deleteThreadEmoji(Long emojiId, User user);

}
