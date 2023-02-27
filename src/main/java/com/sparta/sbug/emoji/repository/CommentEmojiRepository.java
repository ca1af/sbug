package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.CommentEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {

}
