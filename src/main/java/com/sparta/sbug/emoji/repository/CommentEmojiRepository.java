package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {
    Optional<CommentEmoji> findByIdAndUser(Long id, User user);
}
