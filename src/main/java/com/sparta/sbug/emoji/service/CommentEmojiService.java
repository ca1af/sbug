package com.sparta.sbug.emoji.service;

import com.sparta.sbug.user.entity.User;

public interface CommentEmojiService {

    /**
     * 대상 댓글에 이모지 반응을 생성합니다
     *
     * @param emojiType 이모지 종류
     * @param user      반응한 유저
     * @param commentId 대상 댓글
     */
    void createCommentEmoji(String emojiType, User user, Long commentId);

    /**
     * 대상 댓글에 이모지 반응을 삭제합니다
     *
     * @param emojiType 이모지 종류
     * @param user      반응한 유저
     * @param commentId 대상 댓글
     */
    void deleteCommentEmoji(String emojiType, User user, Long commentId);
}