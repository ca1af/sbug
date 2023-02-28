package com.sparta.sbug.emoji.repository.query;

import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;

import java.util.List;

public interface CommentEmojiQuery {

    /**
     * 대상 코멘트에 요청자가 이미 남긴 이모지가 있는지 확인하기 위한 쿼리
     *
     * @param commentId 대상 코멘트
     * @param userId    요청자
     * @param emojiType 이모지 종류
     * @return CommentEmoji : 코멘트 이미지가 있으면 반환하고 없으면 null을 반환
     */
    CommentEmoji getCommentEmojiOrNull(Long commentId, Long userId, EmojiType emojiType);

    /**
     * 코멘트 목록의 이모지 카운트를 조회하기 위한 쿼리
     *
     * @param commentIds 대상 코멘트 ID 목록
     * @return 코멘트 목록의 이모지 카운트
     */
    List<EmojiCountDto> getCommentEmojiCount(List<Long> commentIds);
}
