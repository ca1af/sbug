package com.sparta.sbug.emoji.repository.query;

import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.ThreadEmoji;

import java.util.List;

public interface ThreadEmojiQuery {
    /**
     * 대상 쓰레드에 요청자가 이미 남긴 이모지가 있는지 확인하기 위한 쿼리
     *
     * @param threadId  대상 쓰레드
     * @param userId    요청자
     * @param emojiType 이모지 종류
     * @return ThreadEmoji (이미 반응이 있으면 반환하고 없으면 null 을 반환)
     */
    ThreadEmoji getThreadEmojiOrNull(Long threadId, Long userId, EmojiType emojiType);

    /**
     * 대상 쓰레드에 남겨진 이모지의 개수를 구하기 위한 쿼리
     *
     * @param threadId 대상 코멘트
     * @return List&lt;EmojiCountDto&gt;
     */
    List<EmojiCountDto> getThreadEmojiCount(Long threadId);

    /**
     * 쓰레드 목록의 이모지 카운트를 조회하기 위한 쿼리
     *
     * @param threadIds 대상 코멘트 ID 목록
     * @return 쓰레드 목록의 이모지 카운트
     */
    List<EmojiCountDto> getThreadsEmojiCount(List<Long> threadIds);
}
