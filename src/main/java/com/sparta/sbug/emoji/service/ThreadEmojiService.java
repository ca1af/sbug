package com.sparta.sbug.emoji.service;

import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface ThreadEmojiService {

    /**
     * 대상 쓰레드에 이모지 반응을 생성하거나 삭제합니다.
     *
     * @param emojiType 이모지 종류
     * @param user      요청자
     * @param thread    대상 쓰레드
     * @return boolean (true = 성공, false = 삭제)
     */
    boolean reactThreadEmoji(String emojiType, User user, Thread thread);

    /**
     * 대상 쓰레드의 이모지 반응을 조회합니다.
     *
     * @param threadId 대상 쓰레드
     * @return List&lt;EmojicountDto&gt;
     */
    List<EmojiCountDto> getThreadEmojiCount(Long threadId);

    /**
     * 대상 쓰레드들의 이모지 반응을 조회합니다.
     *
     * @param threadIds 대상 코멘드들
     * @return List&lt;EmojicountDto&gt;
     */
    List<EmojiCountDto> getThreadsEmojiCount(List<Long> threadIds);

    void findAll(String emojiType, Long threadId, Long userId);

}
