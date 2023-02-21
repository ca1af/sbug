package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long> {

}
