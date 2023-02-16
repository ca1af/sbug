package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {

    /**
     * 대상 댓글에 달린 이모지 반응 중 이모지 종류와 반응한 유저 정보가 일치하는 이모지를 찾습니다.
     *
     * @param emojiType 이모지 종류
     * @param comment   대상 댓글
     * @param user      반응한 유저
     * @return Optional&lt;CommentEmoji&gt;
     */
    @Query(value = "select ce from CommentEmoji ce where ce.emojiType = ?1 and ce.comment = ?2 and ce.user = ?3")
    Optional<CommentEmoji> findByEmojiTypeAndCommentAndUser(EmojiType emojiType, Comment comment, User user);
}
