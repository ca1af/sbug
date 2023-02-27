package com.sparta.sbug.emoji.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface CommentEmojiService {

    /**
     * 대상 댓글에 이모지 반응을 생성하거나 삭제합니다.
     *
     * @param emojiType 이모지 종류
     * @param user      반응한 유저
     * @param comment   대상 댓글
     */
    boolean reactCommentEmoji(String emojiType, User user, Comment comment);

    /**
     * 대상 코멘드들의 이모지 반응을 조회합니다.
     *
     * @param commentIds 대상 코멘드들
     * @return List&lt;EmojicountDto&gt;
     */
    List<EmojiCountDto> getCommentEmojiCount(List<Long> commentIds);
}