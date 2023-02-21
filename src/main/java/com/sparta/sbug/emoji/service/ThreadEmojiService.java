package com.sparta.sbug.emoji.service;

import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.user.entity.User;

public interface ThreadEmojiService {

    /**
     * 대상 쓰레드에 이모지 반응을 남깁니다.
     *
     * @param emojiType 이모지 종류
     * @param user      요청자
     * @param threadId  대상 쓰레드 ID
     * @return boolean  true = 성공, false = 삭제
     */
    boolean reactThreadEmoji(String emojiType, User user, Long threadId);

    /**
     * 대상 쓰레드에 이모지 반응을 제거합니다.
     *
     * @param emojiType 이모지 종류
     * @param user      요청자
     * @param threadId  대상 쓰레드 ID
     */
    void deleteThreadEmoji(String emojiType, User user, Long threadId);
    void findAll(String emojiType, Long threadId, Long userId);

}
