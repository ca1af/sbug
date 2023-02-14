package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {
    @Query(value = "select ce from CommentEmoji ce where ce.emojiType = ?1 and ce.comment = ?2 and ce.user = ?3")
    Optional<CommentEmoji> findByEmojiTypeAndCommentAndUser(EmojiType emojiType, Comment comment, User user);
}
